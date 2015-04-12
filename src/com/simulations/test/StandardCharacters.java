package com.simulations.test;

//TODO: Reduce the number of repeat vertices used

public final class StandardCharacters {    
	static final public float charSeparation = 0.075f; //determine amount of spacing between characters
	static final public float whiteSpaceSeparation = 0.275f; //determines spacing for white spaces
	static final public float squareCharRegionLengthWidth = 0.2f; //determines the maximum length and width of character region
	static final float r = squareCharRegionLengthWidth/2;
																				
	static final public float[][] symbols = new float[][]{ 
							
							//?
		new float[]{-r/2, r/2, 0.0f,  -r/2, r, 0.0f,
					-r/2, r, 0.0f,  r/2, r, 0.0f,
					r/2, r, 0.0f,  r/2, r/2, 0.0f,
					r/2, r/2, 0.0f,  0.0f, r/2, 0.0f,
					0.0f, r/2, 0.0f,  0.0f, -r/2, 0.0f,
					-r/4.0f, -(3.0f/4.0f)*r, 0.0f,  r/4.0f, -(3.0f/4.0f)*r, 0.0f,
					-r/8.0f, -(7.0f/8.0f)*r, 0.0f,  r/8.0f, -(7.0f/8.0f)*r, 0.0f
		},

							//A
		new float[]{-r, -r, 0.0f,  0.0f, r, 0.0f,
					r, -r, 0.0f,  0, r, 0.0f,
					-r/2, 0.0f, 0.0f,  r/2, 0.0f, 0.0f
		},
		
							//B
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r/2, 0.0f,
				r, r/2, 0.0f,  0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,  r, -r/2, 0.0f,
				r, -r/2, 0.0f,  -r, -r, 0.0f
		},
		
							//C
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f
		},
		
							//D
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, 0.0f, 0.0f,
				-r, -r, 0.0f,  r, 0.0f, 0.0f
		},
		
							//E
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f
		},
		
							//F
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f
		},
		
							//G
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  r, 0.0f, 0.0f,
				r, 0.0f, 0.0f,  0.0f, 0.0f, 0.0f
		},
		
							//H
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f
		},
		
							//I
		new float[]{-r, r, 0.0f,  r, r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f,
				0.0f, r, 0.0f,  0.0f, -r, 0.0f
		},
		
							//J
		new float[]{-r, r, 0.0f,  r, r, 0.0f,
				0.0f, r, 0.0f,  0.0f, -r, 0.0f,
				0.0f, -r, 0.0f,  -r, -r, 0.0f,
				-r, -r, 0.0f,  -r, 0.0f, 0.0f
		},
		
							//K
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				r, r, 0.0f,  -r, 0.0f, 0.0f,
				r, -r, 0.0f,  -r, 0.0f, 0.0f
		},
		
							//L
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f
		},
		
							//M
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f
		},
		
							//N
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  r, r, 0.0f
		},
		
							//O
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  -r, -r, 0.0f
		},
		
							//P
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, 0.0f, 0.0f,
				r, 0.0f, 0.0f,  -r/2, 0.0f, 0.0f
		},
		
							//Q
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, -r*7.0f/8.0f, 0.0f,
				-r, -r, 0.0f,  r*7.0f/8.0f, -r, 0.0f,
				r/4, -r/4, 0.0f,  r, -r, 0.0f
		},
		
							//R
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, 0.0f, 0.0f,
				r, 0.0f, 0.0f,  0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,  r, -r, 0.0f
		},
		
							//S
		new float[]{r, r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  -r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f,
				r, 0.0f, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  -r, -r, 0.0f
		},
		
							//T
		new float[]{-r, r, 0.0f,  r, r, 0.0f,
				0.0f, r, 0.0f,  0.0f, -r, 0.0f
		},
		
							//U
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f
		},
		
							//V
		new float[]{-r, r, 0.0f,  0.0f, -r, 0.0f,
				r, r, 0.0f,  0.0f, -r, 0.0f
		},
		
							//W
		new float[]{-r, r, 0.0f,  -r/2, -r, 0.0f,
				-r/2, -r, 0.0f,  0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,  r/2, -r, 0.0f,
				r/2, -r, 0.0f,  r, r, 0.0f
		},
		
							//X
		new float[]{-r, r, 0.0f,  r, -r, 0.0f,
				r, r, 0.0f,  -r, -r, 0.0f
		},
		
							//Y
		new float[]{-r, r, 0.0f,  0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,  r, r, 0.0f,
				0.0f, 0.0f, 0.0f,  0.0f, -r, 0.0f
		},
		
							//Z
		new float[]{-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  -r, -r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f
		},
		
					//
		
							//0
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  -r, -r, 0.0f
		},
		
							//1
		new float[]{0.0f, -r, 0.0f,  0.0f, r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f,
				0.0f, r, 0.0f,  -r/2, r/2, 0.0f
		},
		
							//2
		new float[]{-r, r/2, 0.0f,  0.0f, r, 0.0f,
				0.0f, r, 0.0f,  r, r/2, 0.0f,
				r, r/2, 0.0f,  -r, -r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f
		},
		
							//3
		new float[]{-r, r/2, 0.0f,  0.0f, r, 0.0f,
				0.0f, r, 0.0f,  r, 0.0f, 0.0f,
				r, 0.0f, 0.0f,  0.0f, 0.0f, 0.0f,
				r, 0.0f, 0.0f,  0.0f, -r, 0.0f,
				0.0f, -r, 0.0f,  -r, -r/2, 0.0f
		},
		
							//4
		new float[]{-r, r, 0.0f,  -r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f,
				0.0f, r, 0.0f,  0.0f, -r, 0.0f
		},
		
							//5
		new float[]{r, r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  -r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  r, -r/2, 0.0f,
				r, -r/2, 0.0f,  0.0f, -r, 0.0f,
				0.0f, -r, 0.0f,  -r, -r, 0.0f
		},
		
							//6
		new float[]{r, r, 0.0f,  -r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  -r, -r, 0.0f,
				-r, -r, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  r, 0.0f, 0.0f
		},
		
							//7
		new float[]{-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  -r, -r, 0.0f
		},
		
							//8
		new float[]{-r, -r, 0.0f,  -r, r, 0.0f,
				-r, r, 0.0f,  r, r, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f,
				r, -r, 0.0f,  -r, -r, 0.0f,
				-r, 0.0f, 0.0f, r, 0.0f, 0.0f
		},
		
							//9
		new float[]{-r, r, 0.0f,  r, r, 0.0f,
				-r, r, 0.0f,  -r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  r, 0.0f, 0.0f,
				r, r, 0.0f,  r, -r, 0.0f
		},
		
							//#
		new float[]{-r/2, r, 0.0f,  -r/2, -r, 0.0f,
				r/2, r, 0.0f,  r/2, -r, 0.0f,
				-r, r/2, 0.0f,  r, r/2, 0.0f,
				-r, -r/2, 0.0f,  r, -r/2, 0.0f
		},
		
							//.
		new float[]{0.0f, -r/4, 0.0f,  0.0f, -r, 0.0f,
		},
		
							//:
		new float[]{0.0f, r, 0.0f,  0.0f, r/2, 0.0f,
				0.0f, -r, 0.0f,  0.0f, -r/2, 0.0f
		},
		
							//-
		new float[]{-r/2, 0.0f, 0.0f,  r/2, 0.0f, 0.0f
		},
		
							//+
		new float[]{-r/2, 0.0f, 0.0f,  r/2, 0.0f, 0.0f,
				0.0f, r/2, 0.0f,  0.0f, -r/2, 0.0f
		},
    };
	
	static final public int maxVertices(){
		int maxVertNum = symbols[0].length/3;
		
		for(int i=0; i<symbols.length; i++){
			if(symbols[i].length/3 > maxVertNum){
				maxVertNum = symbols[i].length/3;
			}
		}
		 return maxVertNum;
	}
	
	//
	private static int copyVertInfo(int symbolIndex, float[] vertArray){
		for(int i=0; i<StandardCharacters.symbols[symbolIndex].length; i++){
			vertArray[i] = StandardCharacters.symbols[symbolIndex][i];
		}		
		return StandardCharacters.symbols[symbolIndex].length;
	}
	public static int loadCharacterVertex(char character, float[] vertArray){
		switch (character) {
			case 'A':	
				return copyVertInfo(1, vertArray);
			case 'B':
				return copyVertInfo(2, vertArray);
			case 'C':	
				return copyVertInfo(3, vertArray);
			case 'D':
				return copyVertInfo(4, vertArray);
			case 'E':	
				return copyVertInfo(5, vertArray);
			case 'F':
				return copyVertInfo(6, vertArray);
			case 'G':	
				return copyVertInfo(7, vertArray);
			case 'H':
				return copyVertInfo(8, vertArray);
			case 'I':	
				return copyVertInfo(9, vertArray);
			case 'J':
				return copyVertInfo(10, vertArray);
			case 'K':	
				return copyVertInfo(11, vertArray);
			case 'L':
				return copyVertInfo(12, vertArray);
			case 'M':	
				return copyVertInfo(13, vertArray);
			case 'N':
				return copyVertInfo(14, vertArray);
			case 'O':	
				return copyVertInfo(15, vertArray);
			case 'P':
				return copyVertInfo(16, vertArray);
			case 'Q':	
				return copyVertInfo(17, vertArray);
			case 'R':
				return copyVertInfo(18, vertArray);
			case 'S':	
				return copyVertInfo(19, vertArray);
			case 'T':
				return copyVertInfo(20, vertArray);
			case 'U':	
				return copyVertInfo(21, vertArray);
			case 'V':
				return copyVertInfo(22, vertArray);
			case 'W':	
				return copyVertInfo(23, vertArray);
			case 'X':
				return copyVertInfo(24, vertArray);
			case 'Y':	
				return copyVertInfo(25, vertArray);
			case 'Z':
				return copyVertInfo(26, vertArray);
			case '0':	
				return copyVertInfo(27, vertArray);
			case '1':
				return copyVertInfo(28, vertArray);
			case '2':	
				return copyVertInfo(29, vertArray);
			case '3':
				return copyVertInfo(30, vertArray);
			case '4':	
				return copyVertInfo(31, vertArray);
			case '5':
				return copyVertInfo(32, vertArray);
			case '6':	
				return copyVertInfo(33, vertArray);
			case '7':
				return copyVertInfo(34, vertArray);
			case '8':	
				return copyVertInfo(35, vertArray);
			case '9':
				return copyVertInfo(36, vertArray);
			case '#':	
				return copyVertInfo(37, vertArray);
			case '.':
				return copyVertInfo(38, vertArray);
			case ':':	
				return copyVertInfo(39, vertArray);
			case '-':
				return copyVertInfo(40, vertArray);
			case '+':
				return copyVertInfo(41, vertArray);
	
			default:
				return copyVertInfo(0, vertArray);
		}
		
	}
}
