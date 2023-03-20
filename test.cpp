//
// Created by Pointer Null on 2023/3/20.
//
//给你一个长度为 n 的数组 nums ，该数组由从 1 到 n 的 不同 整数组成。另给你一个正整数 k 。
//
// 统计并返回 nums 中的 中位数 等于 k 的非空子数组的数目。
//
// 注意：
//
//
// 数组的中位数是按 递增 顺序排列后位于 中间 的那个元素，如果数组长度为偶数，则中位数是位于中间靠 左 的那个元素。
//
//
//
// 例如，[2,3,1,4] 的中位数是 2 ，[8,4,3,5,1] 的中位数是 4 。
//
//
// 子数组是数组中的一个连续部分。
//
//
//
//
// 示例 1：
//
//
//输入：nums = [3,2,1,4,5], k = 4
//输出：3
//解释：中位数等于 4 的子数组有：[4]、[4,5] 和 [1,4,5] 。
//
//
// 示例 2：
//
//
//输入：nums = [2,3,1], k = 3
//输出：1
//解释：[3] 是唯一一个中位数等于 3 的子数组。
//
//
//
//
// 提示：
//
//
// n == nums.length
// 1 <= n <= 10⁵
// 1 <= nums[i], k <= n
// nums 中的整数互不相同
//
//
// Related Topics 数组 哈希表 前缀和 👍 93 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
#include "vector"
#include "map"
using namespace std;

class Solution {
public:
    int find_1(vector<int>arr1,vector<int>arr2,int idx){
        int sum=0;
        for (int i = arr2.size()-1; i >=0 ; --i) {
            for (int j = 0; j <arr1.size() ; ++j) {
                if (arr2[i]>idx&&arr1[j]<idx&&(arr2[i]-arr1[j]-1)%2==0){
                    sum++;
                } else return sum;
            }
        }
        return sum;
    }
    int find_0(vector<int>arr,int idx){
        int sum=0;
        for (int i = arr.size()-1;i>=0; --i){
            if (arr[i]>idx){
                for (int j = 0; j < i; ++j) {
                    if (arr[j]<idx){
                        if ((arr[i]-arr[j]-1)%2!=0)sum++;
                    } else break;
                }
            } else return sum;
        }
        return sum;
    }
    int countSubarrays(vector<int>& nums, int k) {
        vector<int>count(nums.size()+1,0);
        map<int,vector<int>>F;
        int index=-1;
        for (int i = 0; i < nums.size(); ++i) {
            if (nums[i]>k){
                count[i+1]=count[i-1]+1;
            } else if (nums[i]<k){
                count[i+1]=count[i-1]-1;
            } else {
                index=i;
            }
            F[count[i+1]].push_back(i);
        }
        for (auto arr:F) {

        }
        return 0;
    }
};
//leetcode submit region end(Prohibit modification and deletion)
