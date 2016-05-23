//
//  Matrix.m
//  MatrixModel
//
//  Created by Jason on 2/12/13.
//  Copyright (c) 2013 edu.self. All rights reserved.
//

#import "Matrix.h"

@interface Matrix()

@property (nonatomic, strong) NSArray *martixData;

@end

@implementation Matrix

#pragma mark - Initialize the content of Matrix
/*
 *
Initialize the Matrix with the array of NSNumber
 *
 */
-(id)initWithArray:(NSArray *)array{
    self = [self init];
    
    if (self) {
        _martixData = array;
        _column     = [array[0] count];
        _row        = [array count];
    }
    
    return self;
}

#pragma mark - Access the elemnts in the Matrix
/*
 *
 Access the NSNumber at the a particular row index and column index
 *
 */
-(NSNumber *)numberAtRow:(NSUInteger)row column:(NSUInteger)column{
    NSNumber *number = self.martixData[row][column] ;
    return number;
}

#pragma mark - Maths
// @() is the short cut of [[NSNUmber alloc] initWithXXXXX:yy]
/*
 *
Do addition with another Matrix
 *
 */
-(Matrix *)additionWithMartix:(Matrix *)martix{
    
    return [self createMatrixWithValue:^(NSUInteger row, NSUInteger column){
        double thisValue  = [[self numberAtRow:row column:column] doubleValue];
        double addValue   = [[martix numberAtRow:row column:column] doubleValue];
        
        return @(thisValue + addValue);
    }];
}

/*
 *
Subtract this matrix with another matrix
 *
 */
-(Matrix *)subtractionWithMartix:(Matrix *)martix{
    
    return [self createMatrixWithValue:^(NSUInteger row, NSUInteger column){
        double thisValue     = [[self numberAtRow:row column:column] doubleValue];
        double subtractValue = [[martix numberAtRow:row column:column] doubleValue];
        
        return @(thisValue - subtractValue);
    }];
}

/*
 *
ScalarMupltiple this Matrix with a NSNumber
 *
 */
-(Matrix *)scalarMultiplicationWithNumber:(NSNumber *)number{
    
    double scalarConstant = [number doubleValue];
    
    return [self createMatrixWithValue:^(NSUInteger row, NSUInteger column){
        double thisValue  = [[self numberAtRow:row column:column] doubleValue];
        
        return @(thisValue * scalarConstant);
    }];
}

/*
 *
Multply this matrix with another matrix
 *
 */
-(Matrix *)multiplicationWithMatrix:(Matrix *)matrix{
    // numbers at row of this matrix * numbers at the column of the target martix one by one
    return [self createMatrixWithNumberOfRow:self.row numberOfColumn:matrix.column valueAt:^(NSUInteger row, NSUInteger column){
        double sum=0;
        for (int j=0; j<self.column; j++) {
            double thisValue   = [[self numberAtRow:row column:j] doubleValue];
            double targetValue = [[matrix numberAtRow:j column:column] doubleValue];
            sum += thisValue *targetValue;
        }
    
        return @(sum);
    }];
}

/*
 *
Inverse this Matrix to produce another matrix
 *
 */
-(Matrix *)matrixInversion{
    // get the determinate of this matrix
    double det = [self calculateDeterminatWithMatrix:_martixData];
    if (det == 0) {
        return nil;
    }
    double overDet = 1.0/det;
    
    return [self createMatrixWithValue:^(NSUInteger row, NSUInteger column){
        // calculate determinats of cofactor
        // and transponse it, so we calculate the value of determinate of the cofactor at the oppesite site
        NSArray *cofactorMatrixArray = [self getCafactorFromMatrix:_martixData atRow:column atColumn:row];
        double thisValue = overDet * [self calculateDeterminatWithMatrix:cofactorMatrixArray];
        
        // + - -> + - -> + -, etc....
        if ((row + column)%2 == 1) {
            thisValue = -thisValue;
        }
        
        // the value canot be -0 no matter what condition
        if (thisValue == -0) {
            thisValue = 0;
        }
        
        return @(thisValue);
    }];
}

#pragma mark - private recursion method for matrix inversion
/*
 *
Calculate the DeterminateWithMatrix of a matrix array
 *
 */
-(double)calculateDeterminatWithMatrix:(NSArray*)matrixArray{
    NSUInteger order = [matrixArray count];
    
    // stop the recursion when 2*2 matrix
    // return the base determinate value
    if (order == 2) {
        double a = [matrixArray[0][0] doubleValue];
        double b = [matrixArray[0][1] doubleValue];
        double c = [matrixArray[1][0] doubleValue];
        double d = [matrixArray[1][1] doubleValue];
        return (a*d -(b*c));
    }
    
    // the determinate value
    double det = 0;
    
    // cofactor matrix , width and height -1
    // this determinate value is the sum of its cofactor's determinate value when order >2
    for (int j=0; j<order; j++) {
        NSArray *cofactorMatrixArray = [self getCafactorFromMatrix:matrixArray atRow:0 atColumn:j];
        // + / - ?
        double sign = (j%2==1)?-1.0:1.0;
        double thisNumber = [matrixArray[0][j] doubleValue];
        det += sign * thisNumber * [self calculateDeterminatWithMatrix:cofactorMatrixArray];
    }
    
    return det;
}

/*
 *
Get the cofactor from a Matrix
 *
 */
-(NSArray *)getCafactorFromMatrix:(NSArray *)matrixArray atRow:(NSUInteger)row atColumn:(NSUInteger)column{
    // copy the object to a new array expect the object at the specified row and column
    NSUInteger numberOfRow = [matrixArray count];
    NSUInteger numberOfColumn = [[matrixArray objectAtIndex:0] count];
    
    
    NSMutableArray *cofactorMatrixArray = [[NSMutableArray alloc] init];
    for (int i=0; i<numberOfRow; i++) {
        
        // skip unwanted row
        if (i != row) {
            NSMutableArray *rowArray = [[NSMutableArray alloc] init];
            for (int j=0; j<numberOfColumn; j++) {
                
                // skip unwanted column
                if (j != column) {
                    id number = matrixArray[i][j];
                    [rowArray addObject:number];
                }
            }
            
            [cofactorMatrixArray addObject:rowArray];
        }
    }
    
    
    return cofactorMatrixArray;
}


#pragma mark - Higher order function
/*
 *
Higher order function to create a Matrix with input row and input column, it will accept a function to provide the number at a particular row index and column index
 *
 */
-(Matrix *)createMatrixWithNumberOfRow:(NSUInteger)numberRow numberOfColumn:(NSUInteger)numberColumn valueAt:(NSNumber* (^)(NSUInteger, NSUInteger))getNumberHandler{
    
    // create a new matrix where the value at i,j of the new matrix is specified by handler
    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (int i=0; i<numberRow; i++) {
        
        NSMutableArray *row = [[NSMutableArray alloc] init];
        for (int j=0; j<numberColumn; j++) {
            NSNumber *returnValue = getNumberHandler(i, j);
            [row addObject:returnValue];
        }
        [array addObject:row];
    }
    
    return [[Matrix alloc] initWithArray:array];
}

/*
 *
 Higher order function to create a Matrix with the same size as this objec of matrix
 It will accept a function to provide the number at a particular row index and column index
 *
 */
-(Matrix *)createMatrixWithValue:(NSNumber* (^)(NSUInteger, NSUInteger))getNumberHandler{
    return [self createMatrixWithNumberOfRow:self.row numberOfColumn:self.column valueAt:getNumberHandler];
}

#pragma mark - override description
/*
 *
 To provide description of this matrix in form of row and column
 *
 */
-(NSString *)description{
    NSMutableString *string = [[NSMutableString alloc] init];
    
    [string appendString:@"[ \n"];
    for (int i=0; i<self.row; i++) {
        [string appendFormat:@" Row : %d [ ",i];
        for (int j=0; j<self.column; j++) {
            NSNumber *number = [self numberAtRow:i column:j];
            [string appendFormat:@"%f ,",[number doubleValue]];
        }
        [string appendString:@"] \n"];
    }
    
    [string appendString:@"]"];
    
    return string;
}
@end
