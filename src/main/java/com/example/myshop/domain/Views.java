package com.example.myshop.domain;

public final class Views {
    public interface IdName {}
    public interface IdNameSrc extends IdName {}
    public interface FullMessage extends FullLinkedDirectory {}
    public interface FullMessage2 extends FullMessage {}
    public interface Type  extends IdName {}
    public interface OnlyChild extends Type {}
    public interface FullLinkedDirectory extends OnlyChild {}

}
