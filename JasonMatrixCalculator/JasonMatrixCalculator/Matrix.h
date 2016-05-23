//
//  Matrix.h
//  MatrixModel
//
//  Created by Jason on 2/12/13.
//  Copyright (c) 2013 edu.self. All rights reserved.
//


#import <Foundation/Foundation.h>

@class Matrix;

@interface Matrix : NSObject

@property (nonatomic) NSUInteger row;
@property (nonatomic) NSUInteger column;

#pragma mark - Init Matrix
-(id)initWithArray:(NSArray *)array;

#pragma mark - Access elements
-(NSNumber *)numberAtRow:(NSUInteger)row column:(NSUInteger)column;

#pragma mark - Maths
-(Matrix *)additionWithMartix:(Matrix *)martix;
-(Matrix *)subtractionWithMartix:(Matrix *)martix;
-(Matrix *)scalarMultiplicationWithNumber:(NSNumber *)integer;
-(Matrix *)multiplicationWithMatrix:(Matrix *)matrix;
-(Matrix *)matrixInversion;

@end