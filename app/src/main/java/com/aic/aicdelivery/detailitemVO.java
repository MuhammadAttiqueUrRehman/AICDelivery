package com.aic.aicdelivery;

import androidx.annotation.NonNull;

public class detailitemVO {
    private String detailq, detailr, option, status;

    public detailitemVO() {

    }

    public detailitemVO(String detailq, String detailr, String option, String status) {
        this.detailq = detailq;
        this.detailr = detailr;
        this.option = option;
        this.status = status;
    }

    public String getDetailq() {
        return detailq;
    }

    public void setDetailq(String detailq) {
        this.detailq = detailq;
    }

    public String getDetailr() {
        return detailr;
    }

    public void setDetailr(String detailr) {
        this.detailr = detailr;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        /*return super.toString();*/
        return getDetailq();
    }
}
