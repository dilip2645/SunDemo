import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { IRightalign, Rightalign } from 'app/shared/model/rightalign.model';
import { RightalignService } from './rightalign.service';
import { ToasterService, ToasterType, ToasterTitle } from 'app/shared/component/toaster';
import { FormGroup } from '@angular/forms';
import { FormType, SpinnerService } from 'app/shared';
@Component({
  selector: 'jhi-rightalign-update',
  templateUrl: './rightalign-update.component.html'
})
export class RightalignUpdateComponent implements OnInit {
  breadcrumb: object = {};
  isSaving: boolean;
  rightalignForm: FormGroup;
  formType: FormType;
  rightalign: Rightalign;
  panelTitle: String = '';
  editForm = this.fb.group({
    id: [],
    sdf: []
  });

  constructor(
    protected rightalignService: RightalignService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private toaster: ToasterService,
    private spinner: SpinnerService,
    private router: Router
  ) {
    this.formType = FormType.add;
  }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rightalign }) => {
      if (rightalign['id']) {
        this.formType = FormType.update;
        this.updateForm(rightalign);
        this.panelTitle = 'Edit Rightalign';
        this.breadcrumb = {
          home: 'Home',
          menu: 'Entities',
          entity: 'rightalign',
          entityEdit: 'Edit',
          entityUrl: 'rightalign'
        };
      } else {
        this.rightalign = new Rightalign();
        this.panelTitle = 'Add Rightalign';
        this.breadcrumb = {
          home: 'Home',
          menu: 'Entities',
          entity: 'rightalign',
          entityEdit: 'Add',
          entityUrl: 'rightalign'
        };
      }
    });
  }

  updateForm(rightalign: IRightalign) {
    this.editForm.patchValue({
      id: rightalign.id,
      sdf: rightalign.sdf
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const rightalign = this.createFromForm();
    if (rightalign.id !== undefined && rightalign.id !== null) {
      this.subscribeToSaveResponse(this.rightalignService.update(rightalign));
    } else {
      this.subscribeToSaveResponse(this.rightalignService.create(rightalign));
    }
  }

  private createFromForm(): IRightalign {
    return {
      ...new Rightalign(),
      id: this.editForm.get(['id']).value,
      sdf: this.editForm.get(['sdf']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRightalign>>) {
    this.spinner.spin(true);
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }
  delete() {
    if (this.editForm.getRawValue().id !== undefined && this.editForm.getRawValue().id !== '') {
      this.subscribeToDeleteResponse(this.rightalignService.delete(this.editForm.getRawValue().id));
    } else {
      this.onError('Error');
    }
  }
  private subscribeToDeleteResponse(result: Observable<HttpResponse<Rightalign>>) {
    result.subscribe((res: HttpResponse<Rightalign>) => this.onDeleteSuccess(res.body), (res: HttpErrorResponse) => this.onError('Error'));
  }
  private onError(error: any) {
    this.spinner.spin(false);
    this.toaster.toast({
      message: error,
      type: ToasterType.Error,
      title: ToasterTitle.Error
    });
    this.isSaving = false;
  }
  private onDeleteSuccess(result: Rightalign) {
    this.isSaving = false;
    this.toaster.toast({
      message: 'Deleted successfully',
      type: ToasterType.Success,
      title: ToasterTitle.Success
    });
    this.router.navigate(['rightalign']);
  }
  private onSaveSuccess() {
    this.spinner.spin(false);
    this.isSaving = false;
    if (this.editForm.getRawValue().id !== undefined && this.editForm.getRawValue().id !== null) {
      this.toaster.toast({
        message: 'Updated successfully',
        type: ToasterType.Success,
        title: ToasterTitle.Success
      });
    } else {
      this.toaster.toast({
        message: 'Added successfully',
        type: ToasterType.Success,
        title: ToasterTitle.Success
      });
    }
    this.previousState();
  }

  protected onSaveError() {
    this.spinner.spin(false);
    this.isSaving = false;
  }
  closeDeleteModal(deleteModal: any) {
    deleteModal.close();
  }

  openDeleteModal(deleteModal: any) {
    deleteModal.open();
  }
}
