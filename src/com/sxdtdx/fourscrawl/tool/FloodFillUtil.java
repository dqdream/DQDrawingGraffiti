package com.sxdtdx.fourscrawl.tool;

import android.graphics.Bitmap;

/**
 * ºéÌî³äÀà
 * @author dq
 *
 */
public class FloodFillUtil{

	private Bitmap inputImage;
	public Bitmap getInputImage() {
		return inputImage;
	}

	public void setInputImage(Bitmap inputImage) {
		this.inputImage = inputImage;
	}

	private int[] inPixels;
	private int width;
	private int height;
	private int maxStackSize = 500; // will be increased as needed
	private int[] xstack = new int[maxStackSize];
	private int[] ystack = new int[maxStackSize];
	private int stackSize;
    int newColor=0;
	public FloodFillUtil(Bitmap rawImage,int newColor) {
		this.inputImage = rawImage;
		this.newColor=newColor;
		width = rawImage.getWidth();
        height = rawImage.getHeight();
        inPixels = new int[width*height];
        inputImage.getPixels(inPixels, 0, width, 0, 0, width, height);
	}

	public void startFill(int x,int y)
	{
		floodFillScanLineWithStack(x, y, newColor, inputImage.getPixel(x, y));
		updateResult();
	}
	
	
	
	public int getColor(int x, int y)
	{
		int index = y * width + x;
		return inPixels[index];
	}
	
	public void setColor(int x, int y, int newColor)
	{
		int index = y * width + x;
		inPixels[index] = newColor;
	}
	
	public void updateResult()
	{
		inputImage.setPixels(inPixels, 0, width, 0, 0, width, height);
	}
	public void floodFillScanLineWithStack(int x, int y, int newColor, int oldColor)
	{
		if(oldColor == newColor) {
			System.out.println("do nothing !!!, filled area!!");
			return;
		}
	    emptyStack();
	    
	    int y1; 
	    boolean spanLeft, spanRight;
	    push(x, y);
	    
	    while(true)
	    {    
	    	x = popx();
	    	if(x == -1) return;
	    	y = popy();
	        y1 = y;
	        while(y1 >= 0 && getColor(x, y1) == oldColor) y1--; // go to line top/bottom
	        y1++; // start from line starting point pixel
	        spanLeft = spanRight = false;
	        while(y1 < height && getColor(x, y1) == oldColor)
	        {
	        	setColor(x, y1, newColor);
	            if(!spanLeft && x > 0 && getColor(x - 1, y1) == oldColor)// just keep left line once in the stack
	            {
	                push(x - 1, y1);
	                spanLeft = true;
	            }
	            else if(spanLeft && x > 0 && getColor(x - 1, y1) != oldColor)
	            {
	                spanLeft = false;
	            }
	            if(!spanRight && x < width - 1 && getColor(x + 1, y1) == oldColor) // just keep right line once in the stack
	            {
	                push(x + 1, y1);
	                spanRight = true;
	            }
	            else if(spanRight && x < width - 1 && getColor(x + 1, y1) != oldColor)
	            {
	                spanRight = false;
	            } 
	            y1++;
	        }
	    }
	
	}
	
	private void emptyStack() {
		while(popx() != - 1) {
			popy();
		}
		stackSize = 0;
	}

	final void push(int x, int y) {
		stackSize++;
		if (stackSize==maxStackSize) {
			int[] newXStack = new int[maxStackSize*2];
			int[] newYStack = new int[maxStackSize*2];
			System.arraycopy(xstack, 0, newXStack, 0, maxStackSize);
			System.arraycopy(ystack, 0, newYStack, 0, maxStackSize);
			xstack = newXStack;
			ystack = newYStack;
			maxStackSize *= 2;
		}
		xstack[stackSize-1] = x;
		ystack[stackSize-1] = y;
	}
	
	final int popx() {
		if (stackSize==0)
			return -1;
		else
            return xstack[stackSize-1];
	}

	final int popy() {
        int value = ystack[stackSize-1];
        stackSize--;
        return value;
	}


}
