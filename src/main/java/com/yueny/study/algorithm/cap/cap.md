# CAP 原理
## 一个分布式系统最多只能同时满足 一致性（Consistency），可用性（Availability）和分区容错性（Partition tolerance）这三项中的两项。
### (1)一致性（Consistency）
一致性（Consistency），说的是每一个更新成功后，分布式系统中的所有节点，都能读到最新的信息。即所有节点相当于访问同一份内容，这样的系统就被认为是强一致性的。

### (2)可用性（Availability）
可用性（Availability），是每一个请求，都能得到响应。
请求只需要在一定时间内返回即可，结果可以是成功或者失败，也不需要确保返回的是最新版本的信息。

### (3)分区容错性（Partition tolerance）
分区容错性（Partition tolerance），是说在网络中断，消息丢失的情况下，系统照样能够工作。
这里的网络分区是指由于某种原因，网络被分成若干个孤立的区域，而区域之间互不相通。

