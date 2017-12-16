框架整理
# zxing
二维码扫描()，通过layout修改二维码扫描布局。 [journeyapps zxing 二次修改](https://github.com/journeyapps)
##### 调用方法
    new IntentIntegrator(getActivity()).setCaptureActivity(CoreActivity.class).initiateScan();
###### CoreActivity 重写onResume，onPause，onDestroy，onSaveInstanceState
    //初始化
    capture = new CaptureManager(this, barcodeScannerView);
    capture.initializeFromIntent(getIntent(), savedInstanceState);
    capture.decode();
    -------------------------------------------------------------------------
    @Override
     public boolean onSupportNavigateUp() {
          onBackPressed();
          return true;
      }
      
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (barcodeScannerView == null) {
            return super.onKeyDown(keyCode, event);
           }
           return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
        }
###### xml
    <com.journeyapps.barcodescanner.CompoundBarcodeView
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:layout_alignParentBottom="true"
         android:id="@+id/zxing_barcode_scanner"
         app:zxing_use_texture_view="true"/>   
        
##### Activity中接收回调接口，result中为扫描后的结果
    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     //二维码回调
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
         if (result != null) {
            if (result.getContents() != null) {
            }
         }
     }
##### Gradle引用
    compile "com.losg.library:zxing:lastVersion"
#library
   基础库
   
 
