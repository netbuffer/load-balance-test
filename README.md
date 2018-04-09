# load-balance-test

### step
* 在终端中执行:`run-app.cmd 9000 9001 9002`来启动多个app实例
* 以`nginx.conf`中的配置启动nginx
* 启动`localhost`访问
### 问题
* nginx的ip_hash算法使用客户端ipv4地址的前3个octets，同一局域网中的设备访问会被分配到同一个后端server处理
[http://nginx.org/en/docs/http/ngx_http_upstream_module.html#ip_hash](http://nginx.org/en/docs/http/ngx_http_upstream_module.html#ip_hash)