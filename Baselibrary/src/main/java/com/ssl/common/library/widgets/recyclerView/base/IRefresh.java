package com.ssl.common.library.widgets.recyclerView.base;


public interface IRefresh {

    int STATE_NORMAL = 0; //初始化状态
    int STATE_PULL_TO_REFRESH = 1;//拖动中
    int STATE_REFRESHING = 2;//刷新中

    void setState(int state);

}