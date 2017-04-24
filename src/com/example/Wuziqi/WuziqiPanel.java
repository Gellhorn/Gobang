package com.example.Wuziqi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class WuziqiPanel extends View {

	private int mPanelWidth;
	private float mLineHeight;
	private int MAX_LINE = 10;

	private Paint mPaint = new Paint();
	private Bitmap mWhitePiece;
	private Bitmap mBlackPiece;
	private float radioPieceOfLineHeight = 3 * 1.0f/4;

	//白棋先下，当前轮到白棋
	private boolean mIsWhite = true;
	private List<Point> mWhiteArray = new ArrayList<Point>();
	private List<Point> mBlackArray = new ArrayList<Point>();

	//赢法数组
	int[][][] wins= new int[10][10][200];

	//赢法统计数组
	int[] myWin = new int[200];
	int[] computerWin = new int[200];
	private int count;
	//游戏是否结束over
	private boolean over = false;

	//棋盘上两方棋子的标志  0 无子  ；           1  我方 ；              2   电脑
	private int[][] chessBoard = new int[10][10];

	//保存最高得分的i，j值
	int u=0;
	int v=0;

	public WuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//setBackgroundColor(0x44ff0000);
		init();
		Wininit();
	}

	//初始化
	private void init() {
		// TODO Auto-generated method stub
		mPaint.setColor(0x88000000);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
		mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);


	}

	public void Wininit(){

		count = 0;
		//横向赢法统计 
		for(int i=0;i<10;i++){
			for(int j=0;j<6;j++){
				for(int k=0;k<5;k++){
					wins[i][j+k][count]=1;
				}
				count++;
			}
		}
		//纵向赢法统计
		for(int i=0;i<10;i++){
			for(int j=0;j<6;j++){
				for(int k=0;k<5;k++){
					wins[j+k][i][count]=1;
				}
				count++;
			}
		}

		//左上到右下斜线赢法统计
		for(int i=0;i<6;i++){
			for(int j=0;j<6;j++){
				for(int k=0;k<5;k++){
					wins[i+k][j+k][count]=1;
				}
				count++;
			}
		}

		//右上到左下斜线赢法统计
		for(int i=0;i<6;i++){
			for(int j=9;j>3;j--){
				for(int k=0;k<5;k++){
					wins[i+k][j-k][count]=1;
				}
				count++;
			}
		}

		for(int i = 0 ;i<count;i++){
			myWin[i] = 0;
			computerWin[i] = 0;
		}
	}


	//截取屏幕大小为正方形
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub	
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int width = Math.min(widthSize, heightSize);

		if(widthMode == MeasureSpec.UNSPECIFIED ){
			width = heightSize;
		}else if(heightMode == MeasureSpec.UNSPECIFIED){
			width = widthSize;
		}

		//设置棋盘大小，包括边界
		setMeasuredDimension(width, width);

	}

	//当宽高尺寸确定发生改变以后回调此函数
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

		mPanelWidth = w;
		mLineHeight = mPanelWidth*1.0f/MAX_LINE;

		int pieceWidth = (int)(mLineHeight*radioPieceOfLineHeight);

		mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
		mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		//绘制棋盘
		drawBoard(canvas);

		//绘制棋子
		drawpieces(canvas);
	}

	private void drawBoard(Canvas canvas) {
		// TODO Auto-generated method stub
		int w = mPanelWidth;
		float lineHeight = mLineHeight;

		for(int i= 0;i<MAX_LINE;i++){
			int start = (int) (lineHeight/2);
			int end = (int) (w-lineHeight/2);
			int y = (int) ((0.5+i)*lineHeight);
			canvas.drawLine(start, y, end, y, mPaint);
			canvas.drawLine( y,start,y, end, mPaint);
		}
	}


	private void drawpieces(Canvas canvas) {
		// TODO Auto-generated method stub
		for(int i=0,n=mWhiteArray.size(); i<n;i++){
			Point whitePoint = mWhiteArray.get(i);
			canvas.drawBitmap(mWhitePiece, 
					(whitePoint.x+(1-radioPieceOfLineHeight)/2)*mLineHeight,
					(whitePoint.y+(1-radioPieceOfLineHeight)/2)*mLineHeight,null);
		}

		for(int i=0,n=mBlackArray.size(); i<n;i++){
			Point BlackPoint = mBlackArray.get(i);
			canvas.drawBitmap(mBlackPiece, 
					(BlackPoint.x+(1-radioPieceOfLineHeight)/2)*mLineHeight,
					(BlackPoint.y+(1-radioPieceOfLineHeight)/2)*mLineHeight,null);
		}




	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(over){
			return false;
		}
		if(!mIsWhite){
			return false;
		}
		int action = event.getAction();
		if(action == MotionEvent.ACTION_UP){
			int x= (int) event.getX();
			int y = (int) event.getY();

			Point p = getValidPoint(x,y);
			int m = p.x;
			int n = p.y;

			if(mWhiteArray.contains(p)||mBlackArray.contains(p)){
				return false;
			}

			if(mIsWhite){
				mWhiteArray.add(p);
				chessBoard[m][n]=1;
				for(int k = 0; k<count;k++){
					if(wins[m][n][k] == 1){
						myWin[k]++;
						computerWin[k] = 6;
						if(myWin[k]==5){
							Toast.makeText(this.getContext(), "你赢了", 0).show();
							over = true;
						}
					}
				}

				if(!over){
					mIsWhite = !mIsWhite;
					computerAI();
				}
			}
			invalidate();
			return true;
		}
		return true;
	}

	private void computerAI() {
		// TODO Auto-generated method stub

		//保存最高得分
		int max = 0;

		int[][] myScore = new int[10][10];
		int[][] computerScore = new int[10][10];
		//初始化分数值
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				myScore[i][j] = 0;
				computerScore[i][j]=0;
			}
		}

		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				if(chessBoard[i][j] == 0){
					for(int k=0;k<count;k++){
						if(wins[i][j][k]==1){
							//我方得分，计算机拦截
							if(myWin[k]==1){
								myScore[i][j]+=200;
							}else if(myWin[k]==2){
								myScore[i][j]+=400;	
							}else if(myWin[k] == 3){
								myScore[i][j]+=2000;
							}else if(myWin[k] == 4){
								myScore[i][j] += 10000;
							}

							//计算机走法 得分
							if(computerWin[k]==1){
								computerScore[i][j]+=220;
							}else if(computerWin[k]==2){
								computerScore[i][j]+=420;	
							}else if(computerWin[k] == 3){
								computerScore[i][j]+=2100;
							}else if(computerWin[k] == 4){
								computerScore[i][j] += 20000;
							}

						}
					}

					//判断我方最高得分，将最高分数的点获取出来, u，v为计算机要落下的子的坐标
					if(myScore[i][j]>max){
						max = myScore[i][j];
						u = i;
						v = j;
					}else if(myScore[i][j] == max ){
						if(computerScore[i][j]>computerScore[u][v]){
							//认为i，j点比u，v点好
							u = i;
							v = j;
						}
					}

					//判断电脑方最高得分，将最高分数的点获取出来
					if(computerScore[i][j]>max){
						max = computerScore[i][j];
						u = i;
						v = j;
					}else if(computerScore[i][j] == max ){
						if(myScore[i][j]>myScore[u][v]){
							//认为i，j点比u，v点好
							u = i;
							v = j;
						}
					}

				}
			}
		}
		chessBoard[u][v] = 2;
		mBlackArray.add(new Point(u,v));
		invalidate();

		for(int k = 0; k<count;k++){
			if(wins[u][v][k] == 1){
				computerWin[k]++;
				myWin[k] = 6;
				if(computerWin[k]==5){
					Toast.makeText(this.getContext(), "计算机赢了", 0).show();
					over = true;
				}
			}
		}

		if(!over){
			mIsWhite = !mIsWhite;

		}

	}

	private Point getValidPoint(int x, int y) {
		// TODO Auto-generated method stub
		return new Point((int)(x/mLineHeight),(int)(y/mLineHeight));
	}

}
