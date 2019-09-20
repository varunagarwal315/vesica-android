package com.example.varun.vesica.models;

/**
 * Created by varun on 12/10/16.
 */
public class Conversation {
    private String threadName;
    private long threadPk;
    private Integer unreadMessageCount;

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public long getThreadPk() {
        return threadPk;
    }

    public void setThreadPk(long threadPk) {
        this.threadPk = threadPk;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String toString(){
        return this.threadName;
    }

}
