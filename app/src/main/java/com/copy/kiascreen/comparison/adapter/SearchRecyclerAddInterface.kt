package com.copy.kiascreen.comparison.adapter

import com.copy.kiascreen.comparison.vo.SelectedOption

// 용도 확인 후 이름 바꿔야함 06/15
interface SearchRecyclerAddInterface {
    // 모델 비교 추가
    fun onAddBtnClick(stage : Int)

    // 모델 비교 제거
    fun onDelBtnClick(position : Int)

    // 추천 차량 이미지 설정
    fun setRecCarImg()

    // 모델 비교 버튼 활성화
    fun setCompareBtnEnable()

    // comparePager2 아이템 추가 수정 삭제
    fun setCompPager2Item(adapterPosition : Int)

    // headerRecycler 아이템 추가 및 수정
    fun addHeaderRecycler(item : SelectedOption, position: Int)

    // headerRecycler 아이템 제거
    fun delHeaderRecycler(position: Int)
}