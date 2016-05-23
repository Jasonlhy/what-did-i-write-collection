//
//  LHYViewController.m
//  JasonMatrixCalculator
//
//  Created by Jason on 3/20/13.
//  Copyright (c) 2013 edu.self. All rights reserved.
//

#import "LHYViewController.h"
#import "Matrix.h"
#import "LHYMatrixResultViewController.h"

@interface LHYViewController ()

@property (nonatomic)CGPoint originialCenter; // the original center when the view is loaded
@property (nonatomic)NSInteger size; // flag to control the area of collection view

@property (nonatomic, weak)UITextField *textFieldEditing; // remember the textField that the user is editing on

@end

@implementation LHYViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    self.originialCenter = self.view.center;
    self.size = defaultSize;
    [self refreshTheView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UITextFieldDelegate Methods
-(void)textFieldDidBeginEditing:(UITextField *)textField{
    self.textFieldEditing = textField;
}

#pragma mark - UICollectionView Data Source Methods
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    return (_size * _size);
}

#pragma mark - UICollectionViewDelegate Methods
-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *cellIdentifier = @"matrixCell";
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:cellIdentifier forIndexPath:indexPath];
    
    UITextField *textField = (UITextField *)[cell viewWithTag:textFieldTag];
    textField.frame = CGRectMake(0, 0, cell.frame.size.width, cell.frame.size.height);
    textField.delegate = self;
    
    // move up the buttom matrix only
    if (collectionView == self.buttomMatrix) {
        [textField addTarget:self action:@selector(buttomTextFieldBeginningEditing:) forControlEvents:UIControlEventEditingDidBegin];
    }
    
    // user friendly setting, move to another textfields whent he user taps next
    [textField addTarget:self action:@selector(nextButtonIsTapped:) forControlEvents:UIControlEventEditingDidEndOnExit];
    
    return cell;
    
}

#pragma mark - Cusomterize methods handling user interaction
/*
 *
 Move up the view for inputting the data in the buttom matrix
 *
 */
- (IBAction)buttomTextFieldBeginningEditing:(UITextField *)buttomTextField{
    self.view.center = CGPointMake(self.originialCenter.x, centerAfterMove);
}

/*
 *
 When the users is doing text input on a textfield,
 The user will tap the next button in the keyboard to fiish editing,
 and move to the next textfield in the same UICollectionView.
 *
 */
- (IBAction)nextButtonIsTapped:(UITextField *)textField{
    // find the super view which is a UICollectionCell's contentView,
    // user the contentView to find the UICollectionViewCell
    // find the UICollectionViewCell location in the UICollectionView
    
    UIView *contentView = (UIView *)textField.superview;
    UICollectionViewCell *cell = (UICollectionViewCell *)contentView.superview;
    
    // in the top matrix?
    UICollectionView *selectedCollectionView = nil;
    NSIndexPath *path = [self.topMatrix indexPathForCell:cell];
    if (path != nil) {
        selectedCollectionView = self.topMatrix;
    }
    // if not, it should be located in the buttom matrix
    else{
        selectedCollectionView = self.buttomMatrix;
        path = [self.buttomMatrix indexPathForCell:cell];
    }
    
    // find the next location of UICollectionCell and get the textfield in there to be on focus
    NSInteger nextItemIndex = path.item+1;
    if (nextItemIndex == [selectedCollectionView numberOfItemsInSection:0]) {
        // when you reach the last item in that collectionView
        nextItemIndex = 0;
    }

    NSIndexPath *newPath = [NSIndexPath indexPathForItem:nextItemIndex inSection:0];
    UICollectionViewCell *nextCell = [selectedCollectionView cellForItemAtIndexPath:newPath];
    UITextField *nextTextField = (UITextField *)[nextCell viewWithTag:textFieldTag];
    [nextTextField becomeFirstResponder];
}

/*
 *
 Handling the click event of overview button to show the overview
 The view will be repositioned to the original center to show the overview picture of the screen
 including the top and buttom matrix
 *
 */
- (IBAction)overViewButtonClicked:(UIButton *)sender {
    [self changeToOverview];
}

/*
 *
 Handling the stepper value change event which affect the size of the matrix for calculation
 When the stepper value is increased by 1, the size of the matrix will also be increased by 1
 *
 */
- (IBAction)stepperValueIsChanged:(UIStepper *)sender {
    self.size = sender.value;
    [self clearTextFields];
    [self refreshTheView];
    [self changeToOverview];
}

/*
 *
 Handling the resetButton CLicking event.
 When the user taps on the reset button,
 The size of the top and buttomMatrix will be resized to 2.
 And the view will be repositioned to the overview level.
 *
 */
- (IBAction)resetButtonClicked:(UIButton *)sender {
    self.size          = defaultSize;
    self.stepper.value = defaultSize;
    [self clearTextFields];
    [self refreshTheView];
    [self changeToOverview];
}

/*
 *
 Calculate the matrix data input by user
 wehn the user clicked the calculate button
 *
 */
- (IBAction)calculateButtonClicked:(id)sender {
    // create data models with user input
    Matrix *topMatrix    = [self getMatrixFromCollectionView:self.topMatrix];
    Matrix *buttomMatrix = [self getMatrixFromCollectionView:self.buttomMatrix];
    
    // calcalute result based on use input
    Matrix *result = nil;
    NSInteger selectedSegmentIndex = self.segmentedControl.selectedSegmentIndex;
    
    if (selectedSegmentIndex == addIndex) {
        result = [topMatrix additionWithMartix:buttomMatrix];
    }
    else if (selectedSegmentIndex == subIndex){
        result = [topMatrix subtractionWithMartix:buttomMatrix];
    }
    else if (selectedSegmentIndex == multiplyIndex){
        result = [topMatrix multiplicationWithMatrix:buttomMatrix];
    }
    else if (selectedSegmentIndex == invIndex){
        result = [topMatrix matrixInversion];
    }
    
    // only no matrix returned when the matrix cannot do the inversion
    if (result == nil) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Warning" message:@"The matrix cannot do inversion" delegate:nil cancelButtonTitle:@"cancel" otherButtonTitles:@"OK", nil];
        [alert show];
    }
    else{
        // go to next screen when there is a matrix returned
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
        LHYMatrixResultViewController *resultViewController = [storyboard instantiateViewControllerWithIdentifier:@"resultController" ];
        resultViewController.resultMatrix = result;
        [self presentViewController:resultViewController animated:YES completion:nil];
    }

}

/*
 *
 Handling the event when the user change the operator
 It will hide the buttom collection view when the user choose the inv
 *
 */
- (IBAction)changeOperator:(UISegmentedControl *)sender {
    self.buttomMatrix.hidden = (sender.selectedSegmentIndex == invIndex)?YES:NO;
}

#pragma mark - Customerzied helper methods
/*
 *
 Recalculate the size of a collectionView with the size of the user input/ predefined
 The width of the collectionView is the total of collectionCell's width plus total space between the cell in one line.
 The height of the collectionView is the total of collectionCell's height plus total space between two lines.
 *
 */
-(void)resizeCollectionView:(UICollectionView *)collectionView{
    CGRect frame      = collectionView.frame;
    frame.size.width  = matrixCellWidth  * self.size +  miniWidthSpace * (self.size - 1);
    frame.size.height = matrixCellHeight * self.size +  miniHeightSpace * (self.size - 1);
    
    // "center" the collectionView in program
    double totalWidth = self.view.frame.size.width;
    double newX = (totalWidth - frame.size.width ) /2;
    frame.origin.x = newX;
    
    collectionView.frame = frame;
}

/*
 *
 Resize the two collectionViews, topCollectionView and buttomCollectionView
 *
 */
-(void)resizeCollectionViews{
    [self resizeCollectionView:self.topMatrix];
    [self resizeCollectionView:self.buttomMatrix];
}

/*
 *
 Clear the content of the textfields in UICollectionView
 *
 */
-(void)clearTextFieldsInCollectionView:(UICollectionView *)collecionView{
    for (UICollectionViewCell *cell in [collecionView visibleCells]) {
        UITextField *textField = (UITextField *)[cell viewWithTag:textFieldTag];
        textField.text = @"";
    }
}

/*
 *
 Clear the textfields in two collectionView, topCollectionView and buttomCollectionVIew
 *
 */
-(void)clearTextFields{
    [self clearTextFieldsInCollectionView:self.topMatrix];
    [self clearTextFieldsInCollectionView:self.buttomMatrix];
}

/*
 *
 Refresh the presentation view with data 
 This will resize the collectionViews and call them to reloadData
 *
 */
-(void)refreshTheView{
    [self resizeCollectionViews];
    [self.topMatrix reloadData];
    [self.buttomMatrix reloadData];
}

-(void)changeToOverview{
    self.view.center = self.originialCenter;
    [self.textFieldEditing resignFirstResponder];
}
/*
 *
 Get the content of textfileds in collectionView to become the object of Matrix 
 *
 */
-(Matrix *)getMatrixFromCollectionView:(UICollectionView *)collectionView{
    
    // prepare data for matrix
    NSMutableArray *matrixData = [NSMutableArray new];
    for (int i =0 ;i < self.size; i++){
        
        // Get the textfields number value in each collectioncell
        NSMutableArray *row = [NSMutableArray new];
        for (int j=0; j< self.size; j++){
            NSInteger indexOfCells = i*self.size + j;
            NSIndexPath *path          = [NSIndexPath indexPathForItem:indexOfCells inSection:0];
            UICollectionViewCell *cell = [collectionView cellForItemAtIndexPath:path];
            UITextField *textField     = (UITextField *)[cell viewWithTag:textFieldTag];
            [row addObject:@([textField.text doubleValue])];
        }
        
        [matrixData addObject:row];
    }
    return [[Matrix alloc] initWithArray:matrixData];
}

@end
