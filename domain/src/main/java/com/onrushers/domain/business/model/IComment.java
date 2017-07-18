package com.onrushers.domain.business.model;

import java.util.Date;

public interface IComment {

    Integer getId();
    
    String getText();
    
    Date getDate();
    
    Integer getFeedId();
    
    Integer getUserId();

    boolean isMine();

    void compareWithUserId(Integer userId);
}
