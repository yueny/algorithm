# 输入一棵二元查找树，将该二元查找树转换成一个排序的双向链表。
## 要求
不能创建任何新的结点，只调整引用（指针）的指向,只需要写出转换算法即可，请使用java实现其转换，并并给出时间复杂度和空间复杂度。
     9
   /   \
 5       13
/ \     /   \
3  7     11  15
转换成双向链表
3=5=7=9=11=13=15。

## 提示：二叉查找树按中序遍历得到的数据是按顺序排列的，所以要按照中序遍历的顺序把二叉树转换成链表；
二叉树每一个结点有两个指针left，right，和链表的前驱和后继对应的指针正好对应。

### 思路一：
当我们到达某一结点准备调整以该结点为根结点的子树时，先调整其左子树将左子树转换成一个排好序的左子链表，再调整其右子树转换右子链表。最近链接左子链表的最右结点（左子树的最大结点）、当前结点和右子链表的最左结点（右子树的最小结点）。从树的根结点开始递归调整所有结点。
### 思路二：
我们可以中序遍历整棵树。按照这个方式遍历树，比较小的结点先访问。如果我们每访问一个结点，假设之前访问过的结点已经调整成一个排序双向链表，我们再把调整当前结点的指针将其链接到链表的末尾。当所有结点都访问过之后，整棵树也就转换成一个排序双向链表了。
