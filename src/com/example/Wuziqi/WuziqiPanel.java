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

	//�������£���ǰ�ֵ�����
	private boolean mIsWhite = true;
	private List<Point> mWhiteArray = new ArrayList<Point>();
	private List<Point> mBlackArray = new ArrayList<Point>();

	//Ӯ������
	int[][][] wins= new int[10][10][200];

	//Ӯ��ͳ������
	int[] myWin = new int[200];
	int[] computerWin = new int[200];
	private int count;
	//��Ϸ�Ƿ����over
	private boolean over = false;

	//�������������ӵı�־  0 ����  ��           1  �ҷ� ��              2   ����
	private int[][] chessBoard = new int[10][10];

	//������ߵ÷ֵ�i��jֵ
	int u=0;
	int v=0;

	public WuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//setBackgroundColor(0x44ff0000);
		init();
		Wininit();
	}

	//��ʼ��
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
		//����Ӯ��ͳ�� 
		for(int i=0;i<10;i++){
			for(int j=0;j<6;j++){
				for(int k=0;k<5;k++){
					wins[i][j+k][count]=1;
				}
				count++;
			}
		}
		//����Ӯ��ͳ��
		for(int i=0;i<10;i++){
			for(int j=0;j<6;j++){
				for(int k=0;k<5;k++){
					wins[j+k][i][count]=1;
				}
				count++;
			}
		}

		//���ϵ�����б��Ӯ��ͳ��
		for(int i=0;i<6;i++){
			for(int j=0;j<6;j++){
				for(int k=0;k<5;k++){
					wins[i+k][j+k][count]=1;
				}
				count++;
			}
		}

		//���ϵ�����б��Ӯ��ͳ��
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


	//��ȡ��Ļ��СΪ������
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

		//�������̴�С�������߽�
		setMeasuredDimension(width, width);

	}

	//����߳ߴ�ȷ�������ı��Ժ�ص��˺���
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

		//��������
		drawBoard(canvas);

		//��������
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
							Toast.makeText(this.getContext(), "��Ӯ��", 0).show();
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

		//������ߵ÷�
		int max = 0;

		int[][] myScore = new int[10][10];
		int[][] computerScore = new int[10][10];
		//��ʼ������ֵ
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
							//�ҷ��÷֣����������
							if(myWin[k]==1){
								myScore[i][j]+=200;
							}else if(myWin[k]==2){
								myScore[i][j]+=400;	
							}else if(myWin[k] == 3){
								myScore[i][j]+=2000;
							}else if(myWin[k] == 4){
								myScore[i][j] += 10000;
							}

							//������߷� �÷�
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

					//�ж��ҷ���ߵ÷֣�����߷����ĵ��ȡ����, u��vΪ�����Ҫ���µ��ӵ�����
					if(myScore[i][j]>max){
						max = myScore[i][j];
						u = i;
						v = j;
					}else if(myScore[i][j] == max ){
						if(computerScore[i][j]>computerScore[u][v]){
							//��Ϊi��j���u��v���
							u = i;
							v = j;
						}
					}

					//�жϵ��Է���ߵ÷֣�����߷����ĵ��ȡ����
					if(computerScore[i][j]>max){
						max = computerScore[i][j];
						u = i;
						v = j;
					}else if(computerScore[i][j] == max ){
						if(myScore[i][j]>myScore[u][v]){
							//��Ϊi��j���u��v���
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
					Toast.makeText(this.getContext(), "�����Ӯ��", 0).show();
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
