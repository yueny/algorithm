package com.yueny.study.algorithm.tree;

/**
 *  1、输入一棵二元查找树，将该二元查找树转换成一个排序的双向链表。
 * 要求不能创建任何新的结点，只调整引用（指针）的指向,只需要写出转换算法即可，请使用java实现其转换，并并给出时间复杂度和空间复杂度。
 *      9
 *    /   \
 *  5       13
 * / \     /   \
 * 3  7     11  15
 * 转换成双向链表
 * 3=5=7=9=11=13=15。
 *
 *
 */
public class BstChangeToLink {

    public static class Node{
        private int value;
        private Node leftNode;
        private Node rightNode;

        public Node(int value, Node leftNode, Node rightNode){
            this.value = value;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }
    }

    private Node head,tail;

    public void traversal(Node node){
        if(node==null) return;
        if(node.leftNode!=null) traversal(node.leftNode);
        changeNode(node);
        if(node.rightNode!=null) traversal(node.rightNode);
    }

    private void changeNode(Node node) {
        //初始时，双向链表中无节点，head及tail均为null
        if(head == null){
            head = node;
            tail = node;
        }else{
            //将新node的左指针指向当前tail,再将当前tail的右指针指向新node，最后将tail后移
            node.leftNode = tail;
            tail.rightNode = node;
            tail = node;
        }
    }

    //头结点向后打印
    private void printHead() {
        while(head!=null){
            System.out.print(head.value+" ");
            head = head.rightNode;
        }
    }

    //尾节点向前打印
    private void printTail(){
        while(tail!=null){
            System.out.print(tail.value+" ");
            tail = tail.leftNode;
        }
    }

    public static void main(final String[] args) {
        /**
         *      9
         *    /   \
         *  5       13
         * / \     /   \
         * 3  7     11  15
         */
        Node node11 = new Node(11,null,null);
        Node node15 = new Node(15,null,null);
        Node node3 = new Node(3,null,null);
        Node node7 = new Node(7,null,null);

        Node node5 = new Node(5,node3,node7);
        Node node13 = new Node(13,node11,node15);
        Node node9 = new Node(9,node5,node13);


        BstChangeToLink t = new BstChangeToLink();
        t.traversal(node9);

        System.out.println("双向链表从头结点向后遍历:");
        t.printHead();

        System.out.println();
        System.out.println("双向链表从尾结点向前遍历:");
        t.printTail();
    }
}
