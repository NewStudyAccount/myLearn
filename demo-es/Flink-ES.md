flink-1.20.1
flink-cdc-3.5.0

flink cdc 需要使用flink

flink 操作 bin目录中执行脚本
```json
启动
./start-cluster.sh 
关闭
./stop-cluster.sh 
```

```json
flink 页面  localhost:8081
```

flink cdc 创建  mysql-to-es.yaml
```yaml
# =================================================
# SG 专用 3.5.0-dist 版「按表名分索引」终极 yaml
# 复制粘贴即 100% 成功 + 自动创建 myproject_backup_user、myproject_backup_order 等索引
# =================================================

source:
  type: mysql
  hostname: 127.0.0.1
  port: 3307
  username: flinkcdc
  password: 123456
  tables: myproject.sys_menu
  server-id: 5400-5405


  # 所有 scan 参数必须平铺
  # scan-startup-mode: initial
  # scan-incremental-snapshot-chunk-size: 10000
  # scan-snapshot-fetch-size: 1000


sink:
  type: elasticsearch
  name: Elasticsearch Sink
  hosts: http://127.0.0.1:9200
  version: 8
  batch.size.max.bytes: 52428800    # 50 MB
  record.size.max.bytes: 20971520   # 20 MB
  batch.size.max: 1000
  buffered.requests.max: 2000      # ✅ 2000 > 1000
  buffer.time.max.ms: 2000
  inflight.requests.max: 5





route:
  - source-table: myproject.sys_menu
    sink-table: default_index
    description: sync myproject.sys_menu table to default_index
pipeline:
  name: MySQL to Elasticsearch Pipeline
  parallelism: 1
```


lib 目录中上传
mysql驱动 与flink cdc pipeline 连接器
1. [x] mysql-connector-java-8.0.27.jar
2. [x] flink-cdc-pipeline-connector-elasticsearch-3.5.0.jar
3. [x] flink-cdc-pipeline-connector-mysql-3.5.0.jar


**执行flick cdc 任务**
1. 首先设置 Flink home
```
export FLINK_HOME=/opt/flink/flink-1.20.1
```

2. flick cdc bin目录执行相应的任务yaml
```json
./flink-cdc.sh mysql-to-es.yaml 
```