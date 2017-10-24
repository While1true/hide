package com.kxjsj.doctorassistant.JavaBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vange on 2017/10/23.
 */

public class DoctorBean implements Parcelable {

    /**
     * content : {"age":36,"department":"榧荤","name":"绋嬫煇","occupation":"1","remark":"绋嬶細棣栧腑鐢风涓撳銆佷腑鍥芥�у浼氫細鍛�  姣曚笟浜庢箹鍖楀尰瀛﹂櫌锛屼粠浜嬩复搴婂伐浣滃崄浣欏勾锛屾湁鐫�涓板瘜鐨勪复搴婄粡楠岋紝瀵规硨灏垮绉戝父瑙佺梾銆佸鍙戠梾鍜岀枒闅炬潅鐥囨湁鐙壒瑙佽В銆�  鎿呴暱锛氬墠鍒楄吅鐐庛�佺敺鎬т笉鑲层�佹�у姛鑳介殰纰嶃�佺敓娈栨暣褰㈠強娉屽翱绯荤粺鎰熸煋鐨勮瘖鐤楋紝灏ゅ叾瀵瑰井鍒涙墜鏈紙濡傦細闃磋寧鑳屼晶绁炵粡闃绘柇鏈紝闃磋寧闈欒剦妞嶅煁鏈紝闃磋寧寤堕暱鏈紝鍖呯毊銆佸寘鑼庢暣褰㈡湳锛夋湁杈冮珮鐨勯�犺锛岀粨鍚堝浗鍐呭鍓嶆部鐢锋�ф�у績鐞嗗拰鐢熺悊瀛︽妸鎻℃不鐤楄棰嗭紝鎴愬姛鍦版不鎰堜簡鏁板崈渚嬪悇绉嶅師鍥犳墍鑷寸殑鎬у姛鑳介殰纰嶆偅鑰咃紝娣卞彈鎮ｈ�呬竴鑷村ソ璇勩��","sex":"1","title":"涓讳换","userid":13959012996,"userimg":"http://img0.imgtn.bdimg.com/it/u=2272064340,1125992472&fm=11&gp=0.jpg","username":"绋嬪尰鐢�"}
     * message : 榧荤
     * type : 1
     */

    private ContentBean content;
    private String message;
    private int type;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class ContentBean implements Parcelable {
        /**
         * age : 36
         * department : 榧荤
         * name : 绋嬫煇
         * occupation : 1
         * remark : 绋嬶細棣栧腑鐢风涓撳銆佷腑鍥芥�у浼氫細鍛�  姣曚笟浜庢箹鍖楀尰瀛﹂櫌锛屼粠浜嬩复搴婂伐浣滃崄浣欏勾锛屾湁鐫�涓板瘜鐨勪复搴婄粡楠岋紝瀵规硨灏垮绉戝父瑙佺梾銆佸鍙戠梾鍜岀枒闅炬潅鐥囨湁鐙壒瑙佽В銆�  鎿呴暱锛氬墠鍒楄吅鐐庛�佺敺鎬т笉鑲层�佹�у姛鑳介殰纰嶃�佺敓娈栨暣褰㈠強娉屽翱绯荤粺鎰熸煋鐨勮瘖鐤楋紝灏ゅ叾瀵瑰井鍒涙墜鏈紙濡傦細闃磋寧鑳屼晶绁炵粡闃绘柇鏈紝闃磋寧闈欒剦妞嶅煁鏈紝闃磋寧寤堕暱鏈紝鍖呯毊銆佸寘鑼庢暣褰㈡湳锛夋湁杈冮珮鐨勯�犺锛岀粨鍚堝浗鍐呭鍓嶆部鐢锋�ф�у績鐞嗗拰鐢熺悊瀛︽妸鎻℃不鐤楄棰嗭紝鎴愬姛鍦版不鎰堜簡鏁板崈渚嬪悇绉嶅師鍥犳墍鑷寸殑鎬у姛鑳介殰纰嶆偅鑰咃紝娣卞彈鎮ｈ�呬竴鑷村ソ璇勩��
         * sex : 1
         * title : 涓讳换
         * userid : 13959012996
         * userimg : http://img0.imgtn.bdimg.com/it/u=2272064340,1125992472&fm=11&gp=0.jpg
         * username : 绋嬪尰鐢�
         */

        private int age;
        private String department;
        private String name;
        private String occupation;
        private String remark;
        private String sex;
        private String title;
        private String userid;
        private String userimg;
        private String username;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUserimg() {
            return userimg;
        }

        public void setUserimg(String userimg) {
            this.userimg = userimg;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.age);
            dest.writeString(this.department);
            dest.writeString(this.name);
            dest.writeString(this.occupation);
            dest.writeString(this.remark);
            dest.writeString(this.sex);
            dest.writeString(this.title);
            dest.writeString(this.userid);
            dest.writeString(this.userimg);
            dest.writeString(this.username);
        }

        public ContentBean() {
        }

        protected ContentBean(Parcel in) {
            this.age = in.readInt();
            this.department = in.readString();
            this.name = in.readString();
            this.occupation = in.readString();
            this.remark = in.readString();
            this.sex = in.readString();
            this.title = in.readString();
            this.userid = in.readString();
            this.userimg = in.readString();
            this.username = in.readString();
        }

        public static final Parcelable.Creator<ContentBean> CREATOR = new Parcelable.Creator<ContentBean>() {
            @Override
            public ContentBean createFromParcel(Parcel source) {
                return new ContentBean(source);
            }

            @Override
            public ContentBean[] newArray(int size) {
                return new ContentBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.content, flags);
        dest.writeString(this.message);
        dest.writeInt(this.type);
    }

    public DoctorBean() {
    }

    protected DoctorBean(Parcel in) {
        this.content = in.readParcelable(ContentBean.class.getClassLoader());
        this.message = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<DoctorBean> CREATOR = new Parcelable.Creator<DoctorBean>() {
        @Override
        public DoctorBean createFromParcel(Parcel source) {
            return new DoctorBean(source);
        }

        @Override
        public DoctorBean[] newArray(int size) {
            return new DoctorBean[size];
        }
    };
}
