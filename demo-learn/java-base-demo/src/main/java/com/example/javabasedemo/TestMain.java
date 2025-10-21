package com.example.javabasedemo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestMain {


    public static void main(String[] args) {

        int i = lengthOfLongestSubstring("jbpnbwwd");
        System.out.println(i);

    }


    public static int lengthOfLongestSubstring(String s) {
        Set<Character> characterSet = new HashSet<>();
        int left = 0, right = 0 , maxLength = 0;


        char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            left = i;
            right = left ;
            while (right < charArray.length) {
                if (!characterSet.contains(charArray[right])) {
                    characterSet.add(charArray[right]);
                    right++;
                    maxLength = Math.max(maxLength, characterSet.size());
                } else {
                    characterSet.remove(charArray[left]);
                    left++;
                }
            }
        }

        return maxLength;

    }


    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        int length = nums1.length + nums2.length;

        int[] nums = new int[length];

        for (int i = 0; i < nums1.length; i++) {
            nums[i] = nums1[i];
        }
        for (int i = 0; i < nums2.length; i++) {
            nums[i+nums1.length] =(nums2[i]);
        }

        Arrays.sort(nums);
        if (length % 2 == 0){
            return (nums[length/2] + nums[length/2 - 1])/2.0;
        }else {
            return nums[length/2];
        }


    }


}
