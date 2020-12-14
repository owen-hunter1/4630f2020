package com.hunter.owen.myethics;

import com.google.gson.annotations.Expose;

public class EthicTag {
    @Expose
    private String name;
    @Expose
    private int rating;
    @Expose
    private boolean liked;

    public static EthicTag[] createEthicArrayFromArrays(boolean[] likes, int[] ratings, String[] tags) {
        EthicTag[] result = new EthicTag[tags.length];
        for(int i = 0; i < tags.length; i++){
            result[i] = new EthicTag(tags[i], likes[i], ratings[i]);
        }
        return result;
    }

    public EthicTag(String tag, boolean liked, int rating){
        if(rating > 10){
            rating = 10;
        }
        if(rating < 0){
            rating = 0;
        }
        this.liked = liked;
        this.rating = rating;
        this.name = tag;
    }

    public String getName(){
        return name;
    }
    public boolean isLiked(){
        return liked;
    }
    public int getRating(){
        return rating;
    }

    public void setName(String replace) {
        name = replace;
    }
}
