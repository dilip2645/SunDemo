import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AligncheckSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [AligncheckSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [AligncheckSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AligncheckSharedModule {
  static forRoot() {
    return {
      ngModule: AligncheckSharedModule
    };
  }
}
