package com.c323proj10.dherthog;

public interface Item {
    public long getId();
    public String getTitle();
    public String getPath();
    public String getDuration();
    public void setId(long id);
    public void setTitle(String title);
    public void setPath(String path);
    public void setDuration(String duration);
}
