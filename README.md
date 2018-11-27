# MultiProcessEventBus

# 跨进程EventBus基本原理


1. 将进程的Messenger客户端注册到 MessenageCenter中，
2. post消息是，利用Messenger服务端发送到消息中心
3. 消息中心接受到消息之后，遍历Messenger客户端，将消息转发出去


# TODO
- [x] 简单实现跨进程消息投递
- [ ] 思考如何在使用上减去序列化
- [x] 支持EventBus其他的方法

# 用法[参考Demo]

* 进程启动的时候注册 MultiProcessEventBus.instance.register(this)

* 将EventBus.getDefault().post 替换成MultiProcessEventBus.instance.post(TestMessage())

注意，要是序列化的，毕竟要跨进程
