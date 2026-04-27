insert into myproject.sys_menu (menu_id, menu_name, per_code, menu_type, menu_sort, parent_id, path, component, component_name, status)
values  (1, '系统管理', 's', 'M', 1, 0, '/system', null, null, 0),
        (2, '文章管理', 's', 'M', 2, 0, '/blog', null, null, 0),
        (3, 'OSS存储', 's', 'M', 99, 0, '/oss', null, null, 0),
        (4, '代码生成器', 's', 'M', 99, 0, '/generator', null, null, 0),
        (1001, '用户管理', 'test:001', 'C', 1, 1, '/system/user', 'system/user/index', 'User', 0),
        (1002, '角色列表', 'confirm:002', 'C', 2, 1, '/system/role', 'system/role/index', 'Role', 0),
        (1003, '菜单列表', 'test:002', 'C', 3, 1, '/system/menu', 'system/menu/index', 'Menu', 0),
        (2001, '标签列表', 's', 'C', 1, 2, '/blog/tag', 'blog/tag/index', null, 0),
        (3001, 'OSS配置', 's', 'C', 99, 3, '/oss/config', 'oss/config/index', 'OSS配置', 0),
        (3002, 'OSS文件', 's', 'C', 99, 3, '/oss/file', 'oss/file/index', 'OSS文件', 0);


insert into myproject.sys_menu (menu_id, menu_name, per_code, menu_type, menu_sort, parent_id, path, component, component_name, status)
values  (1, '系统管理', 's', 'M', 1, 0, '/system', null, null, 0),
        (2, '文章管理', 's', 'M', 2, 0, '/blog', null, null, 0),
        (3, 'OSS存储', 's', 'M', 99, 0, '/oss', null, null, 0),
        (4, '代码生成器', 's', 'M', 99, 0, '/generator', null, null, 0),
        (1001, '用户管理', 'test:001', 'C', 1, 1, '/system/user', 'system/user/index', 'User', 0),
        (1002, '角色列表', 'confirm:002', 'C', 2, 1, '/system/role', 'system/role/index', 'Role', 0),
        (1003, '菜单列表', 'test:002', 'C', 3, 1, '/system/menu', 'system/menu/index', 'Menu', 0),
        (2001, '分类列表', 's', 'C', 1, 2, '/blog/category', 'blog/category/index', null, 0),
        (3001, 'OSS配置', 's', 'C', 99, 3, '/oss/config', 'oss/config/index', 'OSS配置', 0),
        (3002, 'OSS文件', 's', 'C', 99, 3, '/oss/file', 'oss/file/index', 'OSS文件', 0);