
package tr.mht.wallpaper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Description implements Parcelable {

    @SerializedName("_content")
    @Expose
    private String Content;

    Description(Parcel in) {
        Content = in.readString();
    }

    public static final Parcelable.Creator<Description> CREATOR = new Creator<Description>() {
        public Description createFromParcel(Parcel source) {
            return new Description(source);
        }
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Content);
    }

    /**
     * 
     * @return
     *     The Content
     */
    public String getContent() {
        return Content;
    }

    /**
     * 
     * @param Content
     *     The _content
     */
    public void setContent(String Content) {
        this.Content = Content;
    }

}
