package com.simulations.test;

//TODO: Reduce the number of repeat vertices used

public final class StandardCharacters {    
	static final public float charSparation = 0.275f; //determine amount of spacing between characters
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
		new float[]{0.0f, r, 0.0f,  -r, 0.0f, 0.0f,
				-r, 0.0f, 0.0f,  0.0f, r/4, 0.0f,
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
			if(symbols[i].length > maxVertNum){
				maxVertNum = symbols[i].length;
			}
		}
		 return maxVertNum;
	}
	
	//
	public static int loadCharacterVertex(char character, float[] vertArray){
		switch (character) {
			case 'A':	
				for(int i=0; i<StandardCharacters.symbols[1].length; i++){
					vertArray[i] = StandardCharacters.symbols[1][i];
				}
				return StandardCharacters.symbols[1].length;
			case 'B':
				for(int i=0; i<StandardCharacters.symbols[2].length; i++){
					vertArray[i] = StandardCharacters.symbols[2][i];
				}
				return StandardCharacters.symbols[2].length;
			case 'C':	
				for(int i=0; i<StandardCharacters.symbols[3].length; i++){
					vertArray[i] = StandardCharacters.symbols[3][i];
				}
				return StandardCharacters.symbols[3].length;
			case 'D':
				for(int i=0; i<StandardCharacters.symbols[4].length; i++){
					vertArray[i] = StandardCharacters.symbols[4][i];
				}
				return StandardCharacters.symbols[4].length;
			case 'E':	
				for(int i=0; i<StandardCharacters.symbols[5].length; i++){
					vertArray[i] = StandardCharacters.symbols[5][i];
				}
				return StandardCharacters.symbols[5].length;
			case 'F':
				for(int i=0; i<StandardCharacters.symbols[6].length; i++){
					vertArray[i] = StandardCharacters.symbols[6][i];
				}
				return StandardCharacters.symbols[6].length;
			case 'G':	
				for(int i=0; i<StandardCharacters.symbols[7].length; i++){
					vertArray[i] = StandardCharacters.symbols[7][i];
				}
				return StandardCharacters.symbols[7].length;
			case 'H':
				for(int i=0; i<StandardCharacters.symbols[8].length; i++){
					vertArray[i] = StandardCharacters.symbols[8][i];
				}
				return StandardCharacters.symbols[8].length;
			case 'I':	
				for(int i=0; i<StandardCharacters.symbols[9].length; i++){
					vertArray[i] = StandardCharacters.symbols[9][i];
				}
				return StandardCharacters.symbols[9].length;
			case 'J':
				for(int i=0; i<StandardCharacters.symbols[10].length; i++){
					vertArray[i] = StandardCharacters.symbols[10][i];
				}
				return StandardCharacters.symbols[10].length;
			case 'K':	
				for(int i=0; i<StandardCharacters.symbols[11].length; i++){
					vertArray[i] = StandardCharacters.symbols[11][i];
				}
				return StandardCharacters.symbols[11].length;
			case 'L':
				for(int i=0; i<StandardCharacters.symbols[12].length; i++){
					vertArray[i] = StandardCharacters.symbols[12][i];
				}
				return StandardCharacters.symbols[12].length;
			case 'M':	
				for(int i=0; i<StandardCharacters.symbols[13].length; i++){
					vertArray[i] = StandardCharacters.symbols[13][i];
				}
				return StandardCharacters.symbols[13].length;
			case 'N':
				for(int i=0; i<StandardCharacters.symbols[14].length; i++){
					vertArray[i] = StandardCharacters.symbols[14][i];
				}
				return StandardCharacters.symbols[14].length;
			case 'O':	
				for(int i=0; i<StandardCharacters.symbols[15].length; i++){
					vertArray[i] = StandardCharacters.symbols[15][i];
				}
				return StandardCharacters.symbols[15].length;
			case 'P':
				for(int i=0; i<StandardCharacters.symbols[16].length; i++){
					vertArray[i] = StandardCharacters.symbols[16][i];
				}
				return StandardCharacters.symbols[16].length;
			case 'Q':	
				for(int i=0; i<StandardCharacters.symbols[17].length; i++){
					vertArray[i] = StandardCharacters.symbols[17][i];
				}
				return StandardCharacters.symbols[17].length;
			case 'R':
				for(int i=0; i<StandardCharacters.symbols[18].length; i++){
					vertArray[i] = StandardCharacters.symbols[18][i];
				}
				return StandardCharacters.symbols[18].length;
			case 'S':	
				for(int i=0; i<StandardCharacters.symbols[19].length; i++){
					vertArray[i] = StandardCharacters.symbols[19][i];
				}
				return StandardCharacters.symbols[19].length;
			case 'T':
				for(int i=0; i<StandardCharacters.symbols[20].length; i++){
					vertArray[i] = StandardCharacters.symbols[20][i];
				}
				return StandardCharacters.symbols[20].length;
			case 'U':	
				for(int i=0; i<StandardCharacters.symbols[21].length; i++){
					vertArray[i] = StandardCharacters.symbols[21][i];
				}
				return StandardCharacters.symbols[21].length;
			case 'V':
				for(int i=0; i<StandardCharacters.symbols[22].length; i++){
					vertArray[i] = StandardCharacters.symbols[22][i];
				}
				return StandardCharacters.symbols[22].length;
			case 'W':	
				for(int i=0; i<StandardCharacters.symbols[23].length; i++){
					vertArray[i] = StandardCharacters.symbols[23][i];
				}
				return StandardCharacters.symbols[23].length;
			case 'X':
				for(int i=0; i<StandardCharacters.symbols[24].length; i++){
					vertArray[i] = StandardCharacters.symbols[24][i];
				}
				return StandardCharacters.symbols[24].length;
			case 'Y':	
				for(int i=0; i<StandardCharacters.symbols[25].length; i++){
					vertArray[i] = StandardCharacters.symbols[25][i];
				}
				return StandardCharacters.symbols[25].length;
			case 'Z':
				for(int i=0; i<StandardCharacters.symbols[26].length; i++){
					vertArray[i] = StandardCharacters.symbols[26][i];
				}
				return StandardCharacters.symbols[26].length;
			case '0':	
				for(int i=0; i<StandardCharacters.symbols[27].length; i++){
					vertArray[i] = StandardCharacters.symbols[27][i];
				}
				return StandardCharacters.symbols[27].length;
			case '1':
				for(int i=0; i<StandardCharacters.symbols[28].length; i++){
					vertArray[i] = StandardCharacters.symbols[28][i];
				}
				return StandardCharacters.symbols[28].length;
			case '2':	
				for(int i=0; i<StandardCharacters.symbols[29].length; i++){
					vertArray[i] = StandardCharacters.symbols[29][i];
				}
				return StandardCharacters.symbols[29].length;
			case '3':
				for(int i=0; i<StandardCharacters.symbols[30].length; i++){
					vertArray[i] = StandardCharacters.symbols[30][i];
				}
				return StandardCharacters.symbols[30].length;
			case '4':	
				for(int i=0; i<StandardCharacters.symbols[31].length; i++){
					vertArray[i] = StandardCharacters.symbols[31][i];
				}
				return StandardCharacters.symbols[31].length;
			case '5':
				for(int i=0; i<StandardCharacters.symbols[32].length; i++){
					vertArray[i] = StandardCharacters.symbols[32][i];
				}
				return StandardCharacters.symbols[32].length;
			case '6':	
				for(int i=0; i<StandardCharacters.symbols[33].length; i++){
					vertArray[i] = StandardCharacters.symbols[33][i];
				}
				return StandardCharacters.symbols[33].length;
			case '7':
				for(int i=0; i<StandardCharacters.symbols[34].length; i++){
					vertArray[i] = StandardCharacters.symbols[34][i];
				}
				return StandardCharacters.symbols[34].length;
			case '8':	
				for(int i=0; i<StandardCharacters.symbols[35].length; i++){
					vertArray[i] = StandardCharacters.symbols[35][i];
				}
				return StandardCharacters.symbols[35].length;
			case '9':
				for(int i=0; i<StandardCharacters.symbols[36].length; i++){
					vertArray[i] = StandardCharacters.symbols[36][i];
				}
				return StandardCharacters.symbols[36].length;
			case '#':	
				for(int i=0; i<StandardCharacters.symbols[37].length; i++){
					vertArray[i] = StandardCharacters.symbols[37][i];
				}
				return StandardCharacters.symbols[37].length;
			case '.':
				for(int i=0; i<StandardCharacters.symbols[38].length; i++){
					vertArray[i] = StandardCharacters.symbols[38][i];
				}
				return StandardCharacters.symbols[38].length;
			case ':':	
				for(int i=0; i<StandardCharacters.symbols[39].length; i++){
					vertArray[i] = StandardCharacters.symbols[39][i];
				}
				return StandardCharacters.symbols[39].length;
			case '-':
				for(int i=0; i<StandardCharacters.symbols[40].length; i++){
					vertArray[i] = StandardCharacters.symbols[40][i];
				}
				return StandardCharacters.symbols[40].length;
			case '+':
				for(int i=0; i<StandardCharacters.symbols[41].length; i++){
					vertArray[i] = StandardCharacters.symbols[41][i];
				}
				return StandardCharacters.symbols[41].length;
	
			default:
				for(int i=0; i<StandardCharacters.symbols[0].length; i++){
					vertArray[i] = StandardCharacters.symbols[0][i];
				}
				return StandardCharacters.symbols[0].length;
		}
		
	}
}
