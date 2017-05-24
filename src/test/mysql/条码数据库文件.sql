DELETE FROM cm_menu WHERE ID LIKE '1003%';
INSERT INTO barcode.cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('1003', '1003', '条码管理', '0000', 0, '1003', '', '', '00', 1, '0', '1', '');

INSERT INTO barcode.cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100301', '100301', '条码维护', '1003', 1, '100301', '', '', '00', 2, '1', '1', '');

INSERT INTO barcode.cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100302', '100302', '生产条码打印', '1003', 2, '100302', '', '', '00', 2, '1', '1', '');

INSERT INTO barcode.cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100303', '100303', '发货条码打印', '1003', 3, '100303', '', '', '00', 2, '1', '1', '');

INSERT INTO barcode.cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100304', '100304', '发货单管理', '1003', 4, '100304', '', '', '00', 2, '1', '1', '');

INSERT INTO barcode.cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100305', '100305', '扫码发货', '1003', 5, '100304', '', '', '00', 2, '1', '1', '');




DELETE FROM cm_resource WHERE ID LIKE '1003%';
INSERT INTO barcode.cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('1003', '条码管理', '', '1', '1', '');

INSERT INTO barcode.cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100301', '条码维护', './html/tiaomwh/barcode_weihu.html', '1', '1', '');

INSERT INTO barcode.cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100302', '生产条码打印', './html/tiaomdy/dcsc_tiaomdy.html', '1', '1', '');

INSERT INTO barcode.cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100303', '发货条码打印', './html/tiaomdy/dcsf_tiaomdy.html', '1', '1', '');

INSERT INTO barcode.cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100304', '发货单管理', './html/fahuo/fahuo_guanli.html', '1', '1', '');


INSERT INTO barcode.cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100305', '扫码发货', './html/saoma/saoma_fahuo.html', '1', '1', '');


DELETE FROM cm_role_resource_rel WHERE RESOURCE_ID  LIKE '1003%';
INSERT INTO barcode.cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '1003');

INSERT INTO barcode.cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100301');

INSERT INTO barcode.cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100302');

INSERT INTO barcode.cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100303');
INSERT INTO barcode.cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100304');

INSERT INTO barcode.cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100305');

alter table crm_barcode add  column BARCODE_BQBH VARCHAR (2)   default 'V1' ;