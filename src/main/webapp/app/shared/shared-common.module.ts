import { NgModule } from '@angular/core';

import { AligncheckSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [AligncheckSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [AligncheckSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class AligncheckSharedCommonModule {}
