--组织表
DELETE FROM cm_org;
INSERT INTO cm_org (ID, CODE, NAME, SHORTFOR, HIGHER_ID, ORG_TYPE, DIRECTOR, ADDRESS, TEL, FAX, ZIPCODE, Path, Layer, Detail, STATUS, DESCRIPTION)
VALUES ('0001', '01', '总公司', '总公司', NULL, '0', NULL, '地址测试', '124434', '354343', '343434', '01', 0, '0', '1', 'erer');

--菜单表
DELETE FROM cm_menu;
INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('1001', '1001', '用户管理', '0000', 0, '1001', '', '', '00', 0, '0', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100101', '100101', '个人信息维护', '1001', 1, '100101', '', '', '00', 1, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100102', '100102', '用户管理', '1001', 2, '100102', '', '', '00', 1, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('1002', '1002', '系统管理', '0000', 0, '1002', '', '', '00', 0, '0', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100201', '100201', '机构管理', '1002', 1, '100201', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100202', '100202', '角色管理', '1002', 2, '100202', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100203', '100203', '资源管理', '1002', 3, '100203', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100204', '100204', '菜单管理', '1002', 4, '100204', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('1003', '1003', '条码管理', '0000', 0, '1003', '', '', '00', 0, '0', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100301', '100301', '条码维护', '1003', 1, '100301', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100302', '100302', '生产条码打印', '1003', 2, '100302', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100303', '100303', '生产条码批量打印', '1003', 3, '100303', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100304', '100304', '发货条码打印', '1003', 4, '100304', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100305', '100305', '发货条码批量打印', '1003', 5, '100305', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100306', '100306', '发货单管理', '1003', 6, '100306', '', '', '00', 2, '1', '1', '');

INSERT INTO cm_menu (ID, CODE, NAME, HIGHER_ID, SEQ, RESOURCE_ID, ICON_URI, HINT, Path, Layer, Detail, STATUS, DESCRIPTIOIN)
VALUES ('100307', '100307', '扫码发货', '1003', 7, '100307', '', '', '00', 2, '1', '1', '');

---资源表
DELETE FROM cm_resource;
INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('1001', '用户信息管理', '', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100101', '个人信息维护', './html/system/user_person_manager.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100102', '用户管理', './html/system/user_manager.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('1002', '系统管理', '', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100201', '机构管理', './html/sysmgt/org.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100202', '角色管理', './html/sysmgt/role.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100203', '资源管理', './html/sysmgt/resource.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100204', '菜单管理', './html/sysmgt/menu.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('1003', '条码管理', '', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100301', '条码维护', './html/barcode/tmwh.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100302', '生产条码打印', './html/barcode/dsctmdy.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100303', '发货条码批量打印', './html/barcode/duosctmdy.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100304', '发货条码打印', './html/barcode/dfhtmdy.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100305', '发货条码批量打印', './html/barcode/duofhtmdy.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100306', '发货单管理', './html/barcode/fhdgl.html', '1', '1', '');

INSERT INTO cm_resource (ID, NAME, URI, TYPE, STATUS, DESCRIPTION)
VALUES ('100307', '扫码发货', './html/barcode/smfh.html', '1', '1', '');

---角色表
DELETE FROM cm_role;
INSERT INTO cm_role (ID, CODE, NAME, STATUS, IS_EXT, DESCRIPTION)
VALUES ('0001', 'sysadmin', '系统管理员', '1', '1', 'null');

INSERT INTO cm_role (ID, CODE, NAME, STATUS, IS_EXT, DESCRIPTION)
VALUES ('0002', 'barcode', '条码管理', '1', '1', 'null');

---角色表
DELETE FROM cm_role_resource_rel;
INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '1001');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100101');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100102');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '1002');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100201');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100202');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100203');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100204');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '1003');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100301');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100302');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100303');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100304');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100305');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100306');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0001', '100307');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '1001');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100101');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100102');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '1003');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100301');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100302');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100303');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100304');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100305');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100306');

INSERT INTO cm_role_resource_rel (ROLE_ID, RESOURCE_ID)
VALUES ('0002', '100307');
---用户表
DELETE FROM cm_user;
INSERT INTO cm_user (ID, CODE, ACCOUNT, NAME, ORGID, PASSWD, STATUS, DESCRIPTION, deptName, email)
VALUES ('000001', 'admin', 'admin', '系统管理员', '0001', '21232F297A57A5A743894A0E4A801FC3', '1', '系统自动添加的管理员', '研究所1', 'sss1@126.com');

INSERT INTO cm_user (ID, CODE, ACCOUNT, NAME, ORGID, PASSWD, STATUS, DESCRIPTION, deptName, email)
VALUES ('20161227205818-0d3cfa2677cf45dd', 'barcode', '123', '条码管理员', '001', 'C4CA4238A0B923820DCC509A6F75849B', '1', '1', '轮毂厂', 'xxx@126.com');

---用户角色表
DELETE FROM cm_user_role_rel;
INSERT INTO cm_user_role_rel (USER_ID, ROLE_ID)
VALUES ('000001', '0001');

INSERT INTO cm_user_role_rel (USER_ID, ROLE_ID)
VALUES ('20161227205818-0d3cfa2677cf45dd', '0002');

