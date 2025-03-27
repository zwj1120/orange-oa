-- 创建 orange_oa_user_info表
DROP TABLE IF EXISTS orange_oa_user_info;
CREATE TABLE orange_oa_user_info(
     id INTEGER NOT NULL,
     user_name VARCHAR(255) NOT NULL,
     user_type INT4 NOT NULL,
     PRIMARY KEY (id)
);

COMMENT ON TABLE orange_oa_user_info IS '用户信息表';
COMMENT ON COLUMN orange_oa_user_info.id IS '主键id';
COMMENT ON COLUMN orange_oa_user_info.user_name IS '人员名称';
COMMENT ON COLUMN orange_oa_user_info.user_type IS '人员类别 0:老板 1:管理层 2:甜品师 3:咖啡师 4:面包师 5:面包中工 6:前台 7:兼职 8:学徒 9:其他';

CREATE INDEX orange_oa_user_info_id_idx ON orange_oa_user_info(id);

CREATE SEQUENCE orange_oa_user_info_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE orange_oa_user_info_id_seq OWNED BY orange_oa_user_info.id;
ALTER TABLE ONLY orange_oa_user_info ALTER COLUMN id SET DEFAULT nextval('orange_oa_user_info_id_seq'::regclass);


-- 创建 orange_oa_store_info表
DROP TABLE IF EXISTS orange_oa_store_info;
CREATE TABLE orange_oa_store_info(
     id INTEGER NOT NULL,
     store_name VARCHAR(255) NOT NULL,
     store_type INT4 NOT NULL,
     PRIMARY KEY (id)
);
COMMENT ON TABLE orange_oa_store_info IS '店铺信息表';
COMMENT ON COLUMN orange_oa_store_info.id IS '主键id';
COMMENT ON COLUMN orange_oa_store_info.store_name IS '店铺名称';
COMMENT ON COLUMN orange_oa_store_info.store_type IS '店铺类别 0:甜品店 1:咖啡厅 2:面包房 3:其他';
CREATE INDEX orange_oa_store_info_id_idx ON orange_oa_store_info(id);
CREATE SEQUENCE orange_oa_store_info_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE orange_oa_store_info_id_seq OWNED BY orange_oa_store_info.id;
ALTER TABLE ONLY orange_oa_store_info ALTER COLUMN id SET DEFAULT nextval('orange_oa_store_info_id_seq'::regclass);

-- 创建 orange_oa_procurement_info表
DROP TABLE IF EXISTS orange_oa_procurement_info;

CREATE TABLE orange_oa_procurement_info(
     id INTEGER NOT NULL,
     store_id INT4 NOT NULL,
     user_id INT4 NOT NULL,
     goods_name VARCHAR(255) NOT NULL,
     goods_num INT4 NOT NULL,
     goods_total_price INT4 NOT NULL,
     year INT4 NOT NULL,
     procurement_time INT8 NOT NULL,
     PRIMARY KEY (id)
);

COMMENT ON TABLE orange_oa_procurement_info IS '采购信息表';
COMMENT ON COLUMN orange_oa_procurement_info.id IS '主键id';
COMMENT ON COLUMN orange_oa_procurement_info.store_id IS '店铺id';
COMMENT ON COLUMN orange_oa_procurement_info.user_id IS '人员id';
COMMENT ON COLUMN orange_oa_procurement_info.goods_name IS '商品名称';
COMMENT ON COLUMN orange_oa_procurement_info.goods_num IS '商品数量';
COMMENT ON COLUMN orange_oa_procurement_info.goods_total_price IS '商品总价';
COMMENT ON COLUMN orange_oa_procurement_info.year IS '年份';
COMMENT ON COLUMN orange_oa_procurement_info.procurement_time IS '采购时间';
CREATE INDEX orange_oa_procurement_info_id_idx ON orange_oa_procurement_info(id);
CREATE SEQUENCE orange_oa_procurement_info_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE orange_oa_procurement_info_id_seq OWNED BY orange_oa_procurement_info.id;
ALTER TABLE ONLY orange_oa_procurement_info ALTER COLUMN id SET DEFAULT nextval('orange_oa_procurement_info_id_seq'::regclass);
