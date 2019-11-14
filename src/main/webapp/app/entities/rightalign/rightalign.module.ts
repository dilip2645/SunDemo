import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AligncheckSharedModule, PanelModule, ExportModule, ModalModule, FormModule } from 'app/shared';
import { RightalignListComponent, RightalignUpdateComponent, rightalignRoute } from './';
import { BreadcrumbModule } from 'app/layouts';
const ENTITY_STATES = [...rightalignRoute];
@NgModule({
  imports: [
    AligncheckSharedModule,
    RouterModule.forChild(ENTITY_STATES),
    PanelModule,
    ExportModule,
    ModalModule,
    FormModule,
    BreadcrumbModule
  ],
  declarations: [RightalignListComponent, RightalignUpdateComponent],
  entryComponents: [RightalignListComponent, RightalignUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AligncheckRightalignModule {}
