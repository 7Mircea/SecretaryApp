package com.example.bazadedateexplo;

public class ExploratorClass {
    private int mId;
    private String mNume;
    private String mPrenume;
    private String mCNP;
    private int mNrSpecializari;
    private String mGrad;
    private int mInstructor;
    private int mIDParinte;
    private int mIDClub;
    private String mDataStart;
    private String mDataFinal;

    public ExploratorClass() {
    }

    public ExploratorClass(String string) {
        String[] arr = string.split(" ");
        mId = Integer.parseInt(arr[0]);
        mNume = arr[1];
        mPrenume = arr[2];
        mCNP = arr[3];
        mNrSpecializari = Integer.parseInt(arr[4]);
        mGrad = arr[5];
        mInstructor = Integer.parseInt(arr[6]);
        mIDParinte = Integer.parseInt(arr[7]);
        mIDClub = Integer.parseInt(arr[8]);
        mDataStart = arr[9];
        mDataFinal = arr[10];
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmNume() {
        return mNume;
    }

    public void setmNume(String mNume) {
        this.mNume = mNume;
    }

    public String getmPrenume() {
        return mPrenume;
    }

    public void setmPrenume(String mPrenume) {
        this.mPrenume = mPrenume;
    }

    public String getmCNP() {
        return mCNP;
    }

    public void setmCNP(String mCNP) {
        this.mCNP = mCNP;
    }

    public int getmNrSpecializari() {
        return mNrSpecializari;
    }

    public void setmNrSpecializari(int mNrSpecializari) {
        this.mNrSpecializari = mNrSpecializari;
    }

    public String getmGrad() {
        return mGrad;
    }

    public void setmGrad(String mGrad) {
        this.mGrad = mGrad;
    }

    public int getmInstructor() {
        return mInstructor;
    }

    public void setmInstructor(int mInstructor) {
        this.mInstructor = mInstructor;
    }

    public int getmIDParinte() {
        return mIDParinte;
    }

    public void setmIDParinte(int mIDParinte) {
        this.mIDParinte = mIDParinte;
    }

    public int getmIDClub() {
        return mIDClub;
    }

    public void setmIDClub(int mIDClub) {
        this.mIDClub = mIDClub;
    }

    public String getmDataStart() {
        return mDataStart;
    }

    public void setmDataStart(String mDataStart) {
        this.mDataStart = mDataStart;
    }

    public String getmDataFinal() {
        return mDataFinal;
    }

    public void setmDataFinal(String mDataFinal) {
        this.mDataFinal = mDataFinal;
    }
}
