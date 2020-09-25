package com.example.myshop.domain;

public final class Views {
    public interface IdName {}
    public interface IdNameSrc extends IdName {}
    public interface FullMessage extends IdNameSrc {}
    public interface Type  extends IdName {}
    public interface OnlyChild extends Type {}

}
