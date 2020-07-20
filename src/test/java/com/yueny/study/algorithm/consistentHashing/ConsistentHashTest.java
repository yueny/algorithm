package com.yueny.study.algorithm.consistentHashing;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsistentHashTest {

    @Test
    public void testSelectKey(){
        List<Node> ns = new ArrayList<>();

        /**
         * 以10个物理节点，160个虚拟节点
         */
        for (int i = 0 ;i<10;i++) {
            Node n = new Node("path_"+i, RoleType.FOLLOWER);
            ns.add(n);
//            System.out.println("创建Node:"+n.getPath());
        }
        // 1000个数据对象做测试. 得到10个物理节点时，这1000个数据对象映射结果 result
        Map<Node,Integer> result = select(ns);

        for (Node node:result.keySet()) {
            System.out.println("减少一个节点前,"+node.getPath()+"节点数据对象个数:"+result.get(node));
        }
        System.out.println("======================================");


        /**
         * 变更为 9 个物理节点
         */
        ns = new ArrayList<>();
        for (int i = 0 ;i<9;i++) {
            Node n = new Node("path_"+i,RoleType.FOLLOWER);
            ns.add(n);
//            System.out.println("创建Node:"+n.getPath());
        }
        // 1000个数据对象做测试. 得到9个物理节点时，这1000个数据对象映射结果 result2
        Map<Node,Integer> result2 = select(ns);
        for (Node path:result2.keySet()) {
            System.out.println("减少一个节点后,"+path.getPath()+"节点数据对象个数:"+result2.get(path));
        }
        System.out.println("======================================");

        int num = 0;
        for (Node path:result2.keySet()) {
            System.out.println("减少一个节点后,"+path.getPath()+"节点数据对象个数从"+result.get(path)+"变为"+result2.get(path));
            if(!result2.get(path).equals(result.get(path))){
                num++;
            }
        }

        System.out.println("数据对象迁移比率:"+num/10.0+"%");

        // 存入数据

    }

    private Map<Node,Integer> select(List<Node> ns){
        Map<Node,Integer> result = new HashMap<>();

        List<Trigger> js = new ArrayList<>();
        for (int i = 0 ;i<1000;i++) {
            Trigger j = new Trigger();
            j.setName("name_"+i);
            js.add(j);
        }

        for (Trigger j:js) {
            // 根据存入的数据得到对应节点
            Node node = ConsistentHash.select(j, ns);
            Integer now = result.get(node);
            if(now!=null){
                result.put(node,now+1);
            }else{
                result.put(node,1);
            }

//            System.out.println(j.getName()+"-"+node.getPath());
        }

        return result;
    }
}
