package com.david916.tetris.tetris;

/**
 * Created by david on 2017/12/2.
 */

import java.util.ArrayList;
public class TetrisBlock implements Cloneable{

    public final static int TYPESUM = 7;
    private  int blockType,blockDirection;
    private int color;
    private float x, y;
    private ArrayList<BlockUnit> units = new ArrayList<>();
    private ArrayList<TetrisBlock> blocks = new ArrayList<>();

    public void remove(int j){
        for(int i=units.size()-1;i>=0;i--){
            if((int)((units.get(i).getY()- TetrisView.beginPoint)/BlockUnit.UNITSIZE) == j)
                units.remove(i);
        }
    }
    public boolean canRotate(){
        for(TetrisBlock b:blocks){
            if(canRotate(b)==false){
                return false;
            }
        }
        return true;
    }
    public boolean canRotate(TetrisBlock other){
        for(BlockUnit i:units){
            for(BlockUnit j:other.getUnits() ){
                if(i.canRotate(j) == false){
                    return false;
                }
            }
        }
        return true;
    }

    public void move(int x){
        if(checkCollision_X() <0 && x<0||checkCollision_X()>0&&x>0)
            return;

        if(x > 0)
            setX(getX() + BlockUnit.UNITSIZE);
        else
            setX(getX() - BlockUnit.UNITSIZE);
    }

    public boolean checkCollision_Y() {

        for(BlockUnit u:units){
            if(u.checkOutOfBoundary_Y())
                return true;
        }
        for(TetrisBlock block:blocks){
            if(this == block) {
                continue;
            }
            if(checkCollision_Y(block))
                return true;
        }
        return false;
    }
    public int checkCollision_X() {

        for(BlockUnit u:units){
            if(u.checkOutOfBoundary_X() != 0)
                return u.checkOutOfBoundary_X();
        }
        for(TetrisBlock block:blocks){
            if(this == block)
                continue;
            if(checkCollision_X(block) != 0)
                return checkCollision_X(block);
        }
        return 0;
    }
    public boolean checkCollision_Y(TetrisBlock other){

        for(BlockUnit i: units){
            for(BlockUnit j:other.units){
                if(i == j) {
                    continue;
                }
                if(i.checkVerticalCollision(j))
                    return true;
            }
        }
        return false;
    }
    public int checkCollision_X(TetrisBlock other){

        for(BlockUnit i: units){
            for(BlockUnit j:other.units){
                if(i == j)
                    continue;

                if(i.checkHorizontalCollision(j)!=0)
                    return i.checkHorizontalCollision(j);
            }
        }
        return 0;
    }
    public TetrisBlock(float x,float y){
        this.x = x;
        this.y = y;

        blockType = (int)(Math.random() * TYPESUM) + 1;
        blockDirection = 1;
        color = (int)(Math.random() * 5) + 1;

        switch(blockType){
            case 1:
                for(int i=0;i<4;i++){
                    units.add(new BlockUnit(x + (-2 + i ) * BlockUnit.UNITSIZE , y));
                }
                break;
            case 2:
                units.add(new BlockUnit(x + (-1 + 1 ) * BlockUnit.UNITSIZE , y - BlockUnit.UNITSIZE));
                for(int i=0;i<3;i++){
                    units.add(new BlockUnit(x + (-1 + i ) * BlockUnit.UNITSIZE , y ));
                }
                break;
            case 3:
                for(int i=0;i<2;i++){
                    units.add(new BlockUnit(x + (i-1) * BlockUnit.UNITSIZE,y - BlockUnit.UNITSIZE ));
                    units.add(new BlockUnit(x + (i-1) * BlockUnit.UNITSIZE,y  ));
                }
                break;
            case 4:
                units.add(new BlockUnit(x + (-1 + 0 ) * BlockUnit.UNITSIZE , y - BlockUnit.UNITSIZE));
                for(int i=0;i<3;i++){
                    units.add(new BlockUnit(x + (-1 + i ) * BlockUnit.UNITSIZE , y ));
                }
                break;
            case 5:
                units.add(new BlockUnit(x + (-1 + 2 ) * BlockUnit.UNITSIZE , y - BlockUnit.UNITSIZE));
                for(int i=0;i<3;i++){
                    units.add(new BlockUnit(x + (-1 + i ) * BlockUnit.UNITSIZE , y ));
                }
                break;
            case 6:
                for(int i=0;i<2;i++){
                    units.add(new BlockUnit(x + (-1+i) * BlockUnit.UNITSIZE,y - BlockUnit.UNITSIZE ));
                    units.add(new BlockUnit(x + i * BlockUnit.UNITSIZE,y ));
                }
                break;
            case 7:
                for(int i=0;i<2;i++){
                    units.add(new BlockUnit(x + i * BlockUnit.UNITSIZE,y - BlockUnit.UNITSIZE ));
                    units.add(new BlockUnit(x + ( -1 + i )* BlockUnit.UNITSIZE,y ));
                }
                break;
        }

    }
    public void setX(float x) {
        float dif_x = x - this.x;

        for (BlockUnit u:units){
            u.setX(u.getX() + dif_x);
        }
        this.x = x;
    }

    public void setY(float y) {

        if(checkCollision_Y())
            return;
        float dif_y = y - this.y;
        for (BlockUnit u:units){
            u.setY(u.getY() + dif_y);
        }
        this.y = y;

    }
    public TetrisBlock(TetrisBlock other){
        x = other.x;
        y = other.y;
        color = other.color;
        blockDirection = other.blockDirection;
        blockType = other.blockType;
        blocks = other.blocks;
    }
    @Override
    public TetrisBlock clone(){

        TetrisBlock block = new TetrisBlock(this);
        for(BlockUnit u:getUnits()){
            block.units.add(u.clone());
        }
        return block;
    }
    public ArrayList<TetrisBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<TetrisBlock> blocks) {
        this.blocks = blocks;
    }

    public ArrayList<BlockUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<BlockUnit> units) {
        this.units = units;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public int getBlockDirection() {
        return blockDirection;
    }

    public void setBlockDirection(int blockDirection) {
        this.blockDirection = blockDirection;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void rotate(){
        if(checkCollision_X()!=0 && checkCollision_Y() || blockType == 3)
            return;

        for(BlockUnit u:units){
            float tx = u.getX();
            float ty = u.getY();
            u.setX(-(ty - y) + x);
            u.setY( tx - x + y) ;
        }
    }

}