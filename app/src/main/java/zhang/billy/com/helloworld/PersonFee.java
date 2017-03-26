package zhang.billy.com.helloworld;

/**
 * Created by zhangvae on 2017/3/23.
 */

public class PersonFee {
    private String createtime;
    private double ele1;
    private double fee1;
    private double ele2;
    private double fee2;
    private double ele3;
    private double fee3;
    private double commEle;
    private double commFee;

    public PersonFee () {}


    public PersonFee(String createtime, double ele1, double fee1, double ele2, double fee2, double ele3, double fee3, double commEle, double commFee) {
        this.createtime = createtime;
        this.ele1 = ele1;
        this.fee1 = fee1;
        this.ele2 = ele2;
        this.fee2 = fee2;
        this.ele3 = ele3;
        this.fee3 = fee3;
        this.commEle = commEle;
        this.commFee = commFee;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public double getEle1() {
        return ele1;
    }

    public void setEle1(double ele1) {
        this.ele1 = ele1;
    }

    public double getFee1() {
        return fee1;
    }

    public void setFee1(double fee1) {
        this.fee1 = fee1;
    }

    public double getEle2() {
        return ele2;
    }

    public void setEle2(double ele2) {
        this.ele2 = ele2;
    }

    public double getFee2() {
        return fee2;
    }

    public void setFee2(double fee2) {
        this.fee2 = fee2;
    }

    public double getEle3() {
        return ele3;
    }

    public void setEle3(double ele3) {
        this.ele3 = ele3;
    }

    public double getFee3() {
        return fee3;
    }

    public void setFee3(double fee3) {
        this.fee3 = fee3;
    }

    public double getCommEle() {
        return commEle;
    }

    public void setCommEle(double commEle) {
        this.commEle = commEle;
    }

    public double getCommFee() {
        return commFee;
    }

    public void setCommFee(double commFee) {
        this.commFee = commFee;
    }
}
