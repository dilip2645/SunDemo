/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { AligncheckTestModule } from '../../../test.module';
import { RightalignUpdateComponent } from 'app/entities/rightalign/rightalign-update.component';
import { RightalignService } from 'app/entities/rightalign/rightalign.service';
import { Rightalign } from 'app/shared/model/rightalign.model';

describe('Component Tests', () => {
  describe('Rightalign Management Update Component', () => {
    let comp: RightalignUpdateComponent;
    let fixture: ComponentFixture<RightalignUpdateComponent>;
    let service: RightalignService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AligncheckTestModule],
        declarations: [RightalignUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RightalignUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RightalignUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RightalignService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Rightalign(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Rightalign();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
