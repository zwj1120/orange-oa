#!/bin/bash

#字符环境设置
export LANG="zh_CN.UTF-8"
export LC_CTYPE="zh_CN.UTF-8"
export NLS_LANG="american_america.AL32UTF8"

#颜色设置变量
export SETCOLOR_SUCCESS="echo -en \\033[1;32m"
export SETCOLOR_FAILURE="echo -en \\033[1;31m"
export SETCOLOR_WARNING="echo -en \\033[1;33m"
export SETCOLOR_NORMAL="echo -en \\033[0;39m"

#操作时间（精确到天）
export SETUP_DATE=$(date "+%Y%m%d")

#需要使用的文件
export RELEASE_NOTE_FILE="release-note"
export private_file="globe.common.conf"

#编译平台
export JAVA_FULL_OS_ARCH="allsystem"
USER_DIR=$(pwd)
CURRENT_USER=$(whoami)
INSTALL_PATH=/home/"${CURRENT_USER}"
INSTALL_TYPE=$1
INSTALL_PARAMS_NUM=$#

MODULE_NAME=TSEA
CONTEXT_NAME=tsea
SET_DIR=$INSTALL_PATH/$MODULE_NAME
#配置文件路径
CONF_DIR=${USER_DIR}/${MODULE_NAME}/webapps/${CONTEXT_NAME}/WEB-INF/classes
TOOL_BAK_DIR=
INSTALL_LOG_FILE=$USER_DIR/install.log
#设置最大轮询次数
MAX_EXE_TIMES=3
#首次sleep时间
SETTING_SLEEP_TIME=30
#是否是第一次安装 0:第一次 1:非第一次
IS_FIRST_INSTALL=0
#APP_PROTOCOL=http
JAVA_HOME_TSEA=


#一般日志函数
function LogMsg() {
  time=$(date "+%D %T")
  echo "[$time] : INFO    : $*"
  $SETCOLOR_NORMAL
}

#警告日志
function LogWarnMsg() {
  time=$(date "+%D %T")
  $SETCOLOR_WARNING
  echo "[$time] : WARN    : $*"
  $SETCOLOR_NORMAL
}

#成功日志
function LogSucMsg() {
  time=$(date "+%D %T")
  $SETCOLOR_SUCCESS
  echo "[$time] : SUCCESS : $*"
  $SETCOLOR_NORMAL
}

#错误日志
function LogErrorMsg() {
  time=$(date "+%D %T")
  $SETCOLOR_FAILURE
  echo "[$time] : ERROR   : $*"
  $SETCOLOR_NORMAL
}

#安装提示
function install_usage() {
  LogMsg "---------------------------Usage---------------------------------------"
  LogMsg "-------------------------For Install-----------------------------------"
  LogMsg " sh install.sh <INSTALL_TYPE> <INSTALL_PATH>"
  LogMsg " Example:"
  LogMsg " sh install.sh install          Install the application."
  LogMsg " sh install.sh uninstall        Uninstall the application."
  LogMsg " sh install.sh reinstall        Reinstall the application."
}

function check_install_parameters() {
  if [ ${INSTALL_PARAMS_NUM} -ne 1 ]; then
    #如果传递进来的参数个数不等于1，则记录错误并退出
    LogErrorMsg "[Function: check_install_parameters] Input error ! You must input one params !"
    install_usage
    #退出
    exit 1
  fi

  #安装类型
  if [ "${INSTALL_TYPE}" != "install" -a "${INSTALL_TYPE}" != "uninstall" -a "${INSTALL_TYPE}" != "reinstall" ]; then
    LogErrorMsg "[Function: check_install_parameters] Input error ! The install method you input must be [install], [uninstall], [reinstall]!"
    install_usage
    exit 1
  fi
  return 0
}
check_install_parameters

#功能说明: 检测依赖变量是否存在，其中参数以"A B C"形式提供
function _check_vars_basic() {
  local BADSTATUS=0
  for var in $*; do
    varl="echo \$$var"
    varVaule="$(eval $varl)"
    if [ -z "${varVaule}" ]; then
      LogWarnMsg "[Function: check_vars_basic] $var is not set!!"
      BADSTATUS=1
    fi
  done
  return ${BADSTATUS}
}

#强验证，发现有未设置的变量错误直接退出
function check_vars() {
  #检测参数个数
  if [ $# -lt 1 ]; then
    LogWarnMsg "[Function: check_vars] no parameters! You should input one param at least"
    return 0
  fi

  _check_vars_basic "$*"

  if [ $? -eq 1 ]; then
    LogErrorMsg "[Function: check_vars] unset var found!!"
    exit 1
  fi

  LogMsg "[Function: check_vars] all vars OK"
  return 0
}

#安装失败，恢复原工具目录
function roll_back() {
  LogMsg "install failed,begin roll back......"
  if [ -d ${SET_DIR} ]; then
    if [ -f "${SET_DIR}/${MODULE_NAME}/logs/catalina.out" ]; then
      echo ">>>>>>>>>>>【catalina.out】日志如下>>>>>>>>>>>>>>>>" >> $INSTALL_LOG_FILE
      cat ${SET_DIR}/${MODULE_NAME}/logs/catalina.out >> $INSTALL_LOG_FILE
    fi
    if [ -f "${SET_DIR}/${MODULE_NAME}/logs/system.log" ]; then
      echo ">>>>>>>>>>>【system.log】日志如下>>>>>>>>>>>>>>>>>>" >> $INSTALL_LOG_FILE
      cat ${SET_DIR}/${MODULE_NAME}/logs/system.log >> $INSTALL_LOG_FILE
    fi
    rm -rf ${SET_DIR}
  fi

  killModule "TSEA"
  LogMsg "roll back success......"
  start
  waitForAppStart $MAX_EXE_TIMES $SETTING_SLEEP_TIME "系统运行状态正常." "TSEA"

  if [ $? -ne 0 ]; then
    LogErrorMsg "系统运行检测失败！"
    exit 1
  fi


}

#强类型守护，保证命令执行。否则，回调将会执行
function StrictGuard() {
  if [ $# -lt 1 ] || [ $# -gt 2 ]; then
    return 0
  fi

  local command="$1"
  local callback=""
  if [ $# -eq 2 ]; then
    callback="$2"
  else
    callback="LogErrorMsg \"exec command '$*' failed!!\";exit 1"
  fi

  eval $command
  if [ $? -ne 0 ]; then
    LogWarnMsg "exec command '$command' failed!!Callback: $callback will be called."
    eval $callback
    if [ $? -ne 0 ]; then
      LogErrorMsg "exec callback '$callback' failed!!"
    fi
    exit 1
  fi
  return 0
}

#功能说明: 导入安装时依赖的环境变量
#从配置文件中获取私有变量
function _load_private() {
  dos2unix -q ${private_file}
  lines=$(cat ${private_file} | grep -v "#" | sed 's/[\r\t]*//g' | sed 's/ //g')

  for line in $lines; do
    if [ -z $line ]; then
      continue
    fi

    if ! test $(echo $line | grep "="); then
      LogErrorMsg "[Function: _load_private] Bad format!! line: $line"
      return 1
    fi

    local name=$(echo "$line" | awk -F '=' '{print $1}')
    local value=$(echo "$line" | awk -F '=' '{print $2}')

    if [ -z "$name" ]; then
      LogErrorMsg "[Function: _load_private] Bad format!! line: $line"
      return 1
    fi

    # 若公共变量值为空，则导入私有配置文件中的相应变量，此处不对配置文件中的私有变量参数值做判空校验
    local namel="echo \$$name"
    local evalname=$(eval $namel)
    if [ -z "$evalname" ]; then
        export $name=$value
        LogMsg "export $name=$value"
    else
        sed -i "s#$name=.*#$name=$evalname#g" ${private_file}
    fi
  done
  return 0
}

function load_private() {
  #检测依赖的环境变量
  check_vars "private_file"

  if [ ! -f ${private_file} ]; then
    LogErrorMsg "[Function: export_private] ${private_file} is not exist or a normal file!! Please check your script!!"
    exit 1
  fi

  _load_private
  if [ $? -ne 0 ]; then
    exit 1
  fi

  #必填检测检测
#  check_vars "DB_IP DB_PORT DB_USERNAME DB_PASSWORD DB_SCHEMA DB_NAME APP_PORT APP_IP APP_PROTOCOL LOCAL_CITYCODE BDP_VERSION BDP_IP BDP_HTTPS_OPEN BDP_CAS_TOMCAT_PORT BDP_OPENAPI_SERVER_TOMCAT_PORT"
  check_vars "EXAM_START_TIME EXAM_END_TIME TS_BDP_APP_NAME TAOSHA_URL APP_PROTOCOL FMDB_KEYTAB_PATH FMDB_KRB5_PATH FMDB_JDBC_URL FMDB_PRINCIPAL DB_IP DB_PORT DB_USERNAME DB_PASSWORD DB_SCHEMA DB_NAME APP_PORT APP_IP LOCAL_CITYCODE BDP_VERSION BDP_IP BDP_HTTPS_OPEN BDP_CAS_TOMCAT_PORT BDP_OPENAPI_SERVER_TOMCAT_PORT"

  checkIp $DB_IP
  if [ $? -eq 1 ]; then
    LogErrorMsg "DB_IP: $DB_IP is not a valid ipAddr"
    exit 1
  fi

  checkPort $DB_PORT "DB_PORT"
  checkPort $APP_PORT "APP_PORT"

}

#强类型守护，保证命令执行。否则，将会执行roll_back
function StrictGuardRollBack() {
  StrictGuard "$1" "roll_back"
  return 0
}

##检查ip地址
checkIp() {
  # ip是否符合规则
  isExist=$(echo $1 | grep -E "^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$")
  if [[ -z ${isExist} ]]; then
    LogErrorMsg "ip[${ip}]不符合规范"
    return 1
  else
    return 2
  fi
}

checkPort() {
  if [ -n "$(echo $1 | sed -n "/^[0-9]\+$/p")" ]; then
    #为number
    return 0
  else
    LogErrorMsg "$2: $1 is not a number"
    exit 1
  fi
}


function killModule() {
    PROCESS_NAME=$1
    PID=$(ps -ef | grep java | grep $PROCESS_NAME | awk '{print $2}')
    for LOOP in ${PID}; do
      LogMsg "${PROCESS_NAME} will be killed......"
      kill -9 ${LOOP}
      sleep 5
      if kill -0 ${LOOP} >/dev/null 2>&1; then
        kill -9 ${LOOP}
      fi
    done
}

function stopApp() {
    killModule "TSEA"
}

#功能说明: 核对安装版本是否和已安装版本冲突 相等返0 、旧大于新 返1、旧小于新返2 、 全新安装返3
function cmp_version() {
  #依赖变量检测
  check_vars "SET_DIR USER_DIR"
  # 正在安装的当然版本号
    n_line=$(cat $USER_DIR/$RELEASE_NOTE_FILE | grep ^Version= | head -1)
    NEW_VERSION=$(echo $n_line | cut -d= -f2)

  if [ -d $SET_DIR ] && [ -f $SET_DIR/$RELEASE_NOTE_FILE ]; then
    # 已经安装的历史版本号
    line=$(cat $SET_DIR/$RELEASE_NOTE_FILE | grep ^Version= | head -1)
    HIS_VERSION=$(echo $line | cut -d= -f2)
    if [ $HIS_VERSION == $NEW_VERSION ]; then
      return 0
    fi
    for i in $(seq 1 3); do
      his=$(echo ${HIS_VERSION} | awk -F '.' '{print $'$i'}')
      new=$(echo ${NEW_VERSION} | awk -F '.' '{print $'$i'}')
      if [ $his -gt $new ]; then
        return 1
      elif [ $his -lt $new ]; then
        return 2
      fi
    done
  else
    return 3
  fi
}

# 删除历史版本，只保留最新的三个版本
function RmRddBakDir() {
  declare -a bakFiles
  declare -a bakDates
  arrayIndex=0
  regex="^TSEA_bak_[1-9][0-9]*\.[0-9]*\.[0-9]*_20[0-9]{6}$"
  FILES=$(ls $INSTALL_PATH | grep -E $regex)
  for file in $FILES; do
    if [ ! -d $INSTALL_PATH/$file ]; then
      continue
    fi
    date=$(echo $file | awk -F "_" '{print $7}')
    ## 6这个数字是自己安装目录名称改成备份名称后，用下划线分割后日期所在的位置！！！
    ## 例如：Nebula_DataCenter_BigData_bak_0.1.0_20140101，则这个目录名用下划线分割后，20140101这个日期在第6个位置。
    bakFiles[$arrayIndex]=$file
    bakDates[$arrayIndex]=$date
    arrayIndex=$(expr $arrayIndex + 1)
  done

  if [ $arrayIndex -gt 3 ]; then
    for ((i = 0; i < $arrayIndex; ++i)); do
      for ((j = $i + 1; j < $arrayIndex; ++j)); do
        if [ ${bakDates[$i]} -lt ${bakDates[$j]} ]; then
          tmpName=${bakFiles[$i]}
          tmpDate=${bakDates[$i]}
          bakFiles[$i]=${bakFiles[$j]}
          bakDates[$i]=${bakDates[$j]}
          bakFiles[$j]=$tmpName
          bakDates[$j]=$tmpDate
        fi
      done
    done
    for ((i = 3; i < $arrayIndex; ++i)); do
      rm -rf $INSTALL_PATH/${bakFiles[$i]}
    done
  fi
}

#备份历史版本
bak_his_version() {
  if [ -d ${SET_DIR}_bak_${HIS_VERSION}_${SETUP_DATE} ]; then
    StrictGuardRollBack "rm -rf ${SET_DIR}_bak_${HIS_VERSION}_${SETUP_DATE}"
  fi
  LogMsg "backup ${SET_DIR} to ${SET_DIR}_bak_${HIS_VERSION}_${SETUP_DATE}..."
  TOOL_BAK_DIR=${SET_DIR}_bak_${HIS_VERSION}_${SETUP_DATE}
  StrictGuardRollBack "mv ${SET_DIR} ${TOOL_BAK_DIR}"

  #只保留时间最新的三份
  RmRddBakDir

}

#移动历史上传数据到安装目录
mv_his_file() {
  ##拷贝原本地文件数据源文件
  TOOL_BAK_DIR=${SET_DIR}_bak_${HIS_VERSION}_${SETUP_DATE}
  FILE_DIR=TSEA/webapps/${CONTEXT_NAME}/WEB-INF/classes/file
  if [ -d ${TOOL_BAK_DIR} ] && [ -d ${TOOL_BAK_DIR}/${FILE_DIR} ] && [ "$(ls ${TOOL_BAK_DIR}/${FILE_DIR})" != "" ]; then
    StrictGuardRollBack "mv ${TOOL_BAK_DIR}/${FILE_DIR}/* ${SET_DIR}/${FILE_DIR}"
  fi
}

configuration() {
  LogMsg "configuration start......"

  StrictGuardRollBack "cd  ${USER_DIR}/${MODULE_NAME}/webapps"

  #开始修改配置项内容
  #配置datasource.xml
  sed -i "s#<driver-url>[^?]*#<driver-url>jdbc:postgresql://$DB_IP:$DB_PORT/$DB_NAME#g" ${CONF_DIR}/datasource.xml
  sed -i "s#<user>[^<]*#<user>$DB_USERNAME#g" ${CONF_DIR}/datasource.xml
  sed -i "s#<password>[^<]*#<password>$DB_PASSWORD#g" ${CONF_DIR}/datasource.xml

  #修改配置项内容


  StrictGuardRollBack "cd ${USER_DIR}/TSEA/bin"
  StrictGuardRollBack "dos2unix *.sh" 2>>$INSTALL_LOG_FILE
  StrictGuardRollBack "cd ${USER_DIR}"

  #设置服务端口号
  sed -i "s#APP_PORT=.*#APP_PORT=${APP_PORT}#g" ${USER_DIR}/bin/TSEA.sh

  if [ "${BDP_HTTPS_OPEN}" ] && [ "${BDP_HTTPS_OPEN}" -eq 1 ];then
    export BDP_PROTOCOL="https"
  else
    export BDP_PROTOCOL="http"
  fi


  #设置http协议
  if [ "${APP_PROTOCOL}" == "https" ]; then
    TOMCAT_SERVER_XML=server-https.xml
    TOMCAT_WEB_XML=web-https.xml
    down_https_certs
  elif [ "${APP_PROTOCOL}" == "http" ]; then
    TOMCAT_SERVER_XML=server-http.xml
    TOMCAT_WEB_XML=web-http.xml
  fi
  StrictGuardRollBack "cp -rf ${USER_DIR}/TSEA/conf/${TOMCAT_SERVER_XML} ${USER_DIR}/TSEA/conf/server.xml"
  StrictGuardRollBack "cp -rf ${USER_DIR}/TSEA/conf/${TOMCAT_WEB_XML} ${USER_DIR}/TSEA/conf/web.xml"
  StrictGuardRollBack "cp -rf ${USER_DIR}/TSEA/webapps/tsea/WEB-INF/${TOMCAT_WEB_XML} ${USER_DIR}/TSEA/webapps/tsea/WEB-INF/web.xml"

  #修改tomcat server文件内容
  TMP_PORT=$((${APP_PORT} + 1))
  sed -i "s#<Server port=\"10121\" shutdown=\"SHUTDOWN\">#<Server port=\"${TMP_PORT}\" shutdown=\"SHUTDOWN\">#g" ${USER_DIR}/TSEA/conf/server.xml


  #========bdp配置中刷配置========
  if [ "A$LOCAL_CITYCODE" != "A" ] ; then
      sed -i "s#local.citycode=.*#local.citycode=${LOCAL_CITYCODE}#g" ${CONF_DIR}/system.properties
  fi



  if [ "A$BDP_IP" != "A" ] && [ "A$BDP_CAS_TOMCAT_PORT" != "A" ] ; then
      sed -i "s#login.home=.*#login.home=${BDP_PROTOCOL}://${BDP_IP}:${BDP_CAS_TOMCAT_PORT}/bdp-login#g" ${CONF_DIR}/system.properties
  fi
  if [ "A$BDP_IP" != "A" ] && [ "A$BDP_OPENAPI_SERVER_TOMCAT_PORT" != "A" ] ; then
      sed -i "s#ip.home=.*#ip.home=${BDP_PROTOCOL}://${BDP_IP}:${BDP_OPENAPI_SERVER_TOMCAT_PORT}#g" ${CONF_DIR}/system.properties
  fi

   sed -i "s#local.home=.*#local.home=${APP_PROTOCOL}://${APP_IP}:${APP_PORT}/${CONTEXT_NAME}#g" ${CONF_DIR}/system.properties


  sed -i "s#fmdb.keytab.path=.*#fmdb.keytab.path=${FMDB_KEYTAB_PATH}#g" ${CONF_DIR}/config/exam/exam-spring.properties
  sed -i "s#fmdb.krb5.path=.*#fmdb.krb5.path=${FMDB_KRB5_PATH}#g" ${CONF_DIR}/config/exam/exam-spring.properties
  sed -i "s#fmdb.jdbc.url=.*#fmdb.jdbc.url=${FMDB_JDBC_URL}#g" ${CONF_DIR}/config/exam/exam-spring.properties
  sed -i "s#fmdb.principal=.*#fmdb.principal=${FMDB_PRINCIPAL}#g" ${CONF_DIR}/config/exam/exam-spring.properties
#  sed -i "s#exam.name=.*#exam.name=${EXAM_NAME}#g" ${CONF_DIR}/config/exam/oa-spring.properties
  sed -i "s#ts.url=.*#ts.url=${TAOSHA_URL}#g" ${CONF_DIR}/config/exam/exam-spring.properties

  sed -i "s#taosha.bdp.name=.*#taosha.bdp.name=${TS_BDP_APP_NAME}#g" ${CONF_DIR}/config/exam/exam-spring.properties
  sed -i "s#exam.start.time=.*#exam.start.time=${EXAM_START_TIME}#g" ${CONF_DIR}/config/exam/exam-spring.properties
  sed -i "s#exam.end.time=.*#exam.end.time=${EXAM_END_TIME}#g" ${CONF_DIR}/config/exam/exam-spring.properties


}

#检查命令是否存在
checkCommand() {
  if ! [ -x "$(command -v $1)" ]; then
    LogErrorMsg "系统缺少$1 命令！！！"
    exit 1
  fi
}

#检查端口号是否存在，已存在就杀掉进程
checkAppPort() {
  PORT_USED=$(netstat -an | grep ${APP_PORT} | grep "LISTEN" 2>&1)
  SHUTDOWN_PORT_USED=$(netstat -an | grep "$((${APP_PORT} + 1))" | grep "LISTEN" 2>&1)
  if [ "${PORT_USED}" != "" ]; then
    TMP_PORT=$(lsof -i:${APP_PORT} | awk 'NR==2 {print $2}')
    LogWarnMsg "端口【${APP_PORT}】已存在，即将终止对应进程【${TMP_PORT}】."
    StrictGuardRollBack "kill -9 ${TMP_PORT}"
    if [ $? -ne 0 ]; then
      LogErrorMsg "终止端口【${APP_PORT}】对应进程失败，安装已回退."
      exit 1
    fi
  fi

  if [ "${SHUTDOWN_PORT_USED}" != "" ]; then
    TMP_PORT=$(lsof -i:$((${APP_PORT} + 1)) | awk 'NR==2 {print $2}')
    LogWarnMsg "端口【$((${APP_PORT} + 1))】已存在，即将终止对应进程【${TMP_PORT}】."
    StrictGuardRollBack "kill -9 ${TMP_PORT}"
    if [ $? -ne 0 ]; then
      LogErrorMsg "终止端口【$((${APP_PORT} + 1))】对应进程失败，安装已回退."
    exit 1
    fi
  fi
}

checkEnv() {
  checkCommand dos2unix
  checkCommand lsof
  checkCommand netstat
  checkCommand wget
  checkCommand date

  load_private
  LogSucMsg "启动环境依赖检测通过"
}


waitForAppStart() {
  #最大等待次数
  local max_execute_times=$1
  #首次sleep时间
  local sleep_interval=$2
  #提示信息
  local success_tips=$3

  local CHECK_MODULE_NAME=$4

  local execute_times=1

  # 循环检查App是否启动完成
  until [ $execute_times -gt $max_execute_times ]; do
    LogMsg "第${execute_times}次检测系统运行状态..."
    #LogMsg "等待${sleep_interval}秒..."
    sleep $sleep_interval
    if [ -f "${USER_DIR}/index.html" ]; then
      rm -f "${USER_DIR}/index.html"
    fi
    timeout 30  wget -q --no-check-certificate ${APP_PROTOCOL}://127.0.0.1:${APP_PORT}/${CONTEXT_NAME} -P ${USER_DIR} -O index.html
    sleep 2
    if [ -e "${USER_DIR}/index.html" ] && [ -s "${USER_DIR}/index.html" ]; then
      StrictGuardRollBack "rm -f ${USER_DIR}/index.html"
      LogSucMsg "${success_tips}"
      return 0
    else
      execute_times=$((execute_times + 1))
      sleep_interval=$((sleep_interval * $execute_times))
    fi
  done
  return 1
}

# 修改安装文件夹默认的权限
chmodLinuxFileAuthority() {
  chmod 755 -R ${SET_DIR}
}



# 从BDP下载HTTPS证书
function down_https_certs() {
  # 判读盘古是否开启HTTPS

  if [ "${APP_PROTOCOL}" == "http" ]; then
    return 0
  fi
  TOMCAT_SERVER_CERT_DIR=${USER_DIR}/TSEA/conf/cert
  # 判断盘古证书是否已存在
  if [[ -d ${TOMCAT_SERVER_CERT_DIR} ]]; then
    local jks_count
    local p12_count
    jks_count=$(find "${TOMCAT_SERVER_CERT_DIR}" -type f -name "*.jks" | wc -l)
    p12_count=$(find "${TOMCAT_SERVER_CERT_DIR}" -type f -name "*.p12" | wc -l)
    if [[ ${jks_count} -eq 1 && ${p12_count} -eq 1 ]]; then
      LogMsg "https认证证书已存在，不再下载"
      return 0
    fi
  fi

  LogMsg "装载https证书..."

  # 下载https证书
  local temp_dir="${USER_DIR}/TSEA/tmp"
  mkdir "${temp_dir}"
  LogMsg " ${BDP_PROTOCOL}://${BDP_IP}:${BDP_OPENAPI_SERVER_TOMCAT_PORT}/cas/api/v1/httpsCert/downloadServerCert?ip=${APP_IP} "
  curl -H "appid:bdp" "${BDP_PROTOCOL}://${BDP_IP}:${BDP_OPENAPI_SERVER_TOMCAT_PORT}/cas/api/v1/httpsCert/downloadServerCert?ip=${APP_IP}" > "${temp_dir}/cert.zip"
  unzip -q "${temp_dir}/cert.zip" -d "${temp_dir}"
  if [[ $? -ne 0 ]]; then
    LogMsg "下载的证书无法解压，请检查当前BDP版本是否是4.4.11及以上，低版本不支持https证书下载"
    rm -rf tmp
    exit 1
  fi

  local jks_path
  jks_path=$(find "${temp_dir}" -type f -name "*.jks")
  if [[ -z ${jks_path} ]]; then
    LogMsg "从bdp下载的https证书中未找到jks文件"
    exit 1
  fi

  local p12_path
  p12_path=$(find "${temp_dir}" -type f -name "*.p12")
  if [[ -z ${p12_path} ]]; then
    LogMsg "从bdp下载的https证书中未找到p12文件"
    exit 1
  fi

  # 复制证书到指定目录
  rm -rf "${TOMCAT_SERVER_CERT_DIR}"
  mkdir -p "${TOMCAT_SERVER_CERT_DIR}"
  \cp "${jks_path}" "${p12_path}" "${TOMCAT_SERVER_CERT_DIR}"
  LogMsg "https证书装载完毕"
}



#安装
installation() {
  checkEnv
  cmp_version
  case "$?" in
  #0表示版本相等
  0)
    LogWarnMsg "the setup version is $HIS_VERSION, and equal the currently version! not need to install."
    exit 0
    ;;
  #1新版本低于当前版本
  1)
    LogWarnMsg "the version you will setup is $NEW_VERSION,low than the currectly verion $HIS_VERSION"
    exit 0
    ;;
    #2新版本高于当前版本
  2)
    stopApp
    bak_his_version
    LogMsg "the version you will setup is $NEW_VERSION, the currectly verion $HIS_VERSION"
    IS_FIRST_INSTALL=1
    ;;
  #3表示之前没有安装任何版本
  3)
    LogMsg "the first install..."
    ;;
  #未知
  *)
    LogErrorMsg "compared version, but status unknow. History Version:$HIS_VERSION SETUP Version:$NEW_VERSION"
    exit 1
    ;;
  esac

  LogMsg "install $MODULE_NAME..."
  # 端口检查
  checkAppPort
  #修改配置项
  configuration
  mkdir -p ${SET_DIR}

  StrictGuardRollBack "cp -rf ${USER_DIR}/release-note ${SET_DIR}"
  StrictGuardRollBack "cp -rf ${USER_DIR}/globe.common.conf ${SET_DIR}"
  StrictGuardRollBack "cp -rf ${USER_DIR}/bin ${SET_DIR}"
  StrictGuardRollBack "cp -rf ${USER_DIR}/TSEA ${SET_DIR}"

  #移动历史上传文本数据
  #mv_his_file

  killModule "TSEA"

  chmodLinuxFileAuthority
  start
  waitForAppStart $MAX_EXE_TIMES $SETTING_SLEEP_TIME "系统运行状态正常." "TSEA"

  if [ $? -ne 0 ]; then
    LogErrorMsg "系统运行检测失败，"
    if [ ${IS_FIRST_INSTALL} -ne 0 ]; then
        LogErrorMsg "将会执行roll_back进行回退."
        roll_back
    fi
    exit 1
  fi
}

uninstallation() {
  LogMsg "uninstall $MODULE_NAME..."
  cmp_version
  case "$?" in
  #0表示版本相等
  0)
    stopApp
    bak_his_version
    IS_FIRST_INSTALL=1
    ;;
  #1新版本低于当前版本
  1)
    LogWarnMsg "not currently version, not allow to uninstall!"
    exit 0
    ;;
    #2新版本高于当前版本
  2)
    ###高于当前版本，也允许去卸载
    stopApp
    bak_his_version
    IS_FIRST_INSTALL=1
    ;;
  #3表示之前没有安装任何版本
  3)
    LogWarnMsg "not any version installed, not allow to uninstall!"
    ;;
  #未知
  *)
    LogErrorMsg "compared version, but status unknow. History Version:$HIS_VERSION SETUP Version:$NEW_VERSION"
    exit 1
    ;;
  esac
}

start() {
    if [ -z "$JAVA_HOME_TSEA" ]; then
      StrictGuard "sh ${SET_DIR}/bin/TSEA.sh start"
    else
      StrictGuard "sh ${SET_DIR}/bin/TSEA.sh start ${JAVA_HOME_TSEA}"
    fi
}

case "$INSTALL_TYPE" in
install)
  #安装
  installation
  ;;
reinstall)
  #卸载
  uninstallation
  #安装
  installation
  ;;
uninstall)
  #卸载
  uninstallation
  ;;
*)
  #提示输入错误
  install_usage
  exit 1
  ;;
esac

LogSucMsg "${INSTALL_TYPE} ${MODULE_NAME} successfully!!"
