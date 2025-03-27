#!/bin/bash

#当前路径，即startup.sh文件所在的路径
USER_DIR=$(
  cd $(dirname $0)
  pwd
)
SHELL_NAME="FitMetaDataCenter.sh"
APP_NAME="TSEA"
APP_PORT=10120
TOMCAT_CONFIG_PATH=${USER_DIR}/../TSEA/conf/server.xml

JVM_OPTION="-Xms1024m -Xmx10240m -XX:+HeapDumpOnOutOfMemoryError"

export JRE_HOME=${JAVA_HOME}

replace_port() {
  #  DEFAULT_APP_PORT=10022
  #  if [ ${APP_PORT} -ne ${DEFAULT_APP_PORT} ] ;then
  #    sed -i "69s#<Connector[^<]*#<Connector port=\""${APP_PORT}"\" protocol=\"HTTP/1.1\"#g" ${TOMCAT_CONFIG_PATH}
  #    sed -i "22s#<Server[^<]*#<Server port=\""$[`expr ${APP_PORT} + 1`]"\" shutdown=\"SHUTDOWN\">#g" ${TOMCAT_CONFIG_PATH}
  #  fi
#  sed -i "69s#<Connector[^<]*#<Connector port=\""${APP_PORT}"\" protocol=\"org.apache.coyote.http11.Http11NioProtocol\" URIEncoding=\"UTF-8\" #g" ${TOMCAT_CONFIG_PATH}
  sed -i "69s#<Connector[^<]*#<Connector port=\""${APP_PORT}"\" protocol=\"org.apache.coyote.http11.Http11NioProtocol\" #g" ${TOMCAT_CONFIG_PATH}
  sed -i "22s#<Server[^<]*#<Server port=\""$(($(expr ${APP_PORT} + 1)))"\" shutdown=\"SHUTDOWN\">#g" ${TOMCAT_CONFIG_PATH}
}

checkenv() {
  if [ "$1" ]; then
    export JAVA_HOME=$1
    export JRE_HOME=$1
  fi
  JAVA_VERSION=$(${JAVA_HOME}/bin/java -version 2>&1)
  JAVA_VERSION_NUM=$(expr substr $(echo $JAVA_VERSION | cut -d'"' -f2) 1 3 | sed 's#\.##g')
  if [ -z "${JAVA_VERSION_NUM}" ] || [ $JAVA_VERSION_NUM -ne 18 ]; then
    echo "启动环境检测失败! 没有找到 JDK 1.8，请先安装 JDK 1.8，或指定JDK1.8路径启动(sh eagleEye.sh start <jdk1.8 home path>)"
    exit 1
  fi
  # 端口检查
  #  PORT_USED=`netstat -an | grep ${APP_PORT} | grep "LISTEN" 2>&1`
  #  SHUTDOWN_PORT_USED=`netstat -an | grep "$[${APP_PORT} + 1]" | grep "LISTEN" 2>&1`
  #  if [ "${PORT_USED}" != "" ];then
  #    echo "启动环境检测失败! 端口【${APP_PORT}】已被占用。"
  #    exit 1
  #  fi
  #  if [ "${SHUTDOWN_PORT_USED}" != "" ];then
  #    echo "启动环境检测失败! 端口【$[${APP_PORT} + 1]】已被占用。"
  #    exit 1
  #  fi
  #  echo "启动环境依赖检测通过"
  replace_port
}

usage() {
  echo "$SHELL_NAME <start <java home path> | stop | state | restart <java home path>| version(-v) | checkenv(-env) <java home path>>"
  echo "      start:启动探查工具进程"
  echo "            <java home path>  可指定运行探查工具的jdk home 路径，必须为jdk1.8版本"
  echo "  "
  echo "      stop: 停止探查工具进程"
  echo "  "
  echo "      restart:重启探查工具进程"
  echo "            <java home path>  可指定重启时运行探查工具的jdk home 路径，必须为jdk1.8版本"
  echo "  "
  echo "      state: 检查当前程序运行状态"
  echo "  "
  echo "      version | -v :查看当前程序版本号"
  echo "  "
  echo "      checkenv | -env :检查程序运行依赖环境是否正常"
  echo "           <java home path>  检测指定jdk home是否符合程序启动要求"

}

init() {
  export LANG=en_US.UTF-8
  export LC_ALL=en_US.UTF-8
  ##设置linux最大文件打开数目
  ulimit -n 65535
}

start() {
  PID=$(ps -ef | grep java | grep ${APP_NAME} | awk '{print $2}')
  #进程存在则退出
  if [  -n "${PID}" ]; then
    return 0
  fi
  checkenv "$1"

  cd ${USER_DIR}/../TSEA/bin
  chmod 755 *.sh
  init
  sh startup.sh
  cd ../../bin

}

stop() {
  PID=$(ps -ef | grep java | grep ${APP_NAME} | awk '{print $2}')
  for LOOP in ${PID}; do
    echo "${APP_NAME} will be killed......"
    kill -9 ${LOOP}
    sleep 5
    if kill -0 ${LOOP} >/dev/null 2>&1; then
      kill -9 ${LOOP}
    fi
  done
}

showState() {
  ps -ef --width 4096 | grep ${APP_NAME} | grep -v "grep" | grep -v "${SHELL_NAME}"
}

version() {
  VERSION=$(cat ../release-note | grep Version= | awk -F = '{print $2}')
  echo ${VERSION}
}

test() {
  echo "开始系统运行状态检测..."
  if [ -f "${USER_DIR}/index.html" ]; then
    rm -f "${USER_DIR}/index.html"
  fi
  wget -q --no-check-certificate https://127.0.0.1:${APP_PORT}/exam -P ${USER_DIR} -O index.html
  sleep 2
  if [ ! -s "${USER_DIR}/index.html" ]; then
    echo "系统运行检测失败，请查看系统运行日志"
    exit 1
  else
    rm -f "${USER_DIR}/index.html"
    echo "系统运行状态正常"
    #    LOCAL_IP=`ifconfig -a | grep inet | grep -v 127.0.0.1 | grep -v inet6 | awk '{print $2}' | tr -d "addr:"`
    #    if [ "${LOCAL_IP}" == "" ];then
    #        LOCAL_IP="{本机机器IP}"
    #    fi
    #    echo "系统运行状态正常，请在浏览器登录页面地址：${LOCAL_IP}:${APP_PORT}"
  fi
  exit 0
}

case "$1" in
start)
  start $2
  echo -e "${APP_NAME} 启动结束,10s后进行系统运行状态检测..."
  sleep 10
  test

  ;;
stop)
  stop $2
  echo -e "${APP_NAME} Stopping...\t[OK]"
  ;;
restart)
  stop $2
  sleep 2
  start $2
  echo -e "${APP_NAME} 启动结束,10s后进行系统运行状态检测..."
  sleep 20
  test
  ;;
version | -v)
  version
  ;;
state)
  test
  ;;
checkenv | -env)
  checkenv $2
  ;;
*)
  usage
  exit 1
  ;;
esac

exit 0
