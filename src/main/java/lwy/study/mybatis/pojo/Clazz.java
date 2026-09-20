package lwy.study.mybatis.pojo;

import java.util.Objects;

/**
 * 实体类：对应 design 库里的 class 班级表
 * （class 是 Java 关键字不能作类名，所以取名 Clazz）
 * cid（班级id）、cno（班级编号）、cname（班级名称）、
 * tid（班主任id，关联 teacher 表）
 */
public class Clazz {
    private Integer cid;
    private String cno;
    private String cname;
    private Integer tid;

    public Clazz() {
    }

    public Clazz(Integer cid, String cno, String cname, Integer tid) {
        this.cid = cid;
        this.cno = cno;
        this.cname = cname;
        this.tid = tid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(cid, clazz.cid) && Objects.equals(cno, clazz.cno) &&
                Objects.equals(cname, clazz.cname) && Objects.equals(tid, clazz.tid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, cno, cname, tid);
    }

    @Override
    public String toString() {
        return "Clazz{" +
                "cid=" + cid +
                ", cno='" + cno + '\'' +
                ", cname='" + cname + '\'' +
                ", tid=" + tid +
                '}';
    }
}
