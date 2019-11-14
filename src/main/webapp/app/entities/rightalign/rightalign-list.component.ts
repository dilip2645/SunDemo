import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { QueryParamsStateService } from 'app/shared/util/query-params-state';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IRightalign } from 'app/shared/model/rightalign.model';
import { AccountService } from 'app/core';

import { ExportType, PageData } from 'app/shared';
import { ObservableUnsubscriptionUtilities } from 'app/shared/util/observable-unsubscription-utils';
import { ITEMS_PER_PAGE } from 'app/shared';
import * as FileSaver from 'file-saver';
import { RightalignService } from './rightalign.service';
@Component({
  selector: 'jhi-rightalign',
  templateUrl: './rightalign-list.component.html'
})
export class RightalignListComponent implements OnInit, OnDestroy {
  breadcrumb: object = {};
  // A reference of page size selection form
  public pageSizeSelectionForm: FormGroup;
  public searchFilterForm: FormGroup;
  public pageData: PageData = {
    currentPage: 0,
    totalPages: 0,
    totalItems: 0,
    itemsPerPage: ITEMS_PER_PAGE,
    numItemsOnCurrentPage: 0
  };
  public readonly availablePageSizes: number[] = [10, 20, 30, 40, 50];

  currentAccount: any;
  rightaligns: IRightalign[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  public searchObject: object;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    protected rightalignService: RightalignService,
    protected formBuilder: FormBuilder,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
    this.rightalignService
      .query({
        page: this.page - 1,
        size: this.pageData.itemsPerPage,
        sort: this.sort(),
        ...this.searchObject
      })
      .subscribe(
        (res: HttpResponse<IRightalign[]>) => this.paginateRightaligns(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/rightalign'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/rightalign',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.breadcrumb = {
      home: 'Home',
      menu: 'Entities',
      entity: 'Rightalign'
    };
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.pageSizeSelectionForm = this.formBuilder.group({
      pageSizeSelector: [this.pageData.itemsPerPage]
    });
    this.pageSizeSelectionForm
      .get('pageSizeSelector')
      .valueChanges.pipe(ObservableUnsubscriptionUtilities.untilComponentDestroyed(this))
      .subscribe((pageSize: number) => {
        this.onPageSizeChange(pageSize);
      });
    this.searchFilterForm = this.formBuilder.group({
      id: [''],
      sdf: ['']
    });
    this.registerChangeInRightaligns();
  }

  onPageSizeChange(itemsPerPage: number) {
    this.pageData.itemsPerPage = itemsPerPage;
    // Reset the page number back to 0 to avoid having no records to show
    this.pageData.currentPage = 0;
    this.changeStateParams();
    this.loadAll();
  }

  /**
   * Creates object that will be served as input to createQueryParamsForCurrentUrl
   */
  changeStateParams() {
    if (this.searchObject !== undefined) {
      QueryParamsStateService.setQueryParams(this.activatedRoute, this.router, this.pageData, this.searchObject);
    } else {
      QueryParamsStateService.setQueryParams(this.activatedRoute, this.router, this.pageData);
    }
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  onExportClicked(exportType: ExportType) {
    this.onExportClick(exportType, 'SUN-Report.');
  }

  onExportClick(exportType: ExportType, reportName: string) {
    // const { currentPage, itemsPerPage } = this.pageData;
    //this.spinner.spin(true);
    this.rightalignService
      .exportReport(exportType)
      .pipe(ObservableUnsubscriptionUtilities.untilComponentDestroyed(this))
      .subscribe(
        file => {
          FileSaver.saveAs(
            new Blob([file.body], { type: exportType.toLowerCase() }),
            reportName + (exportType === 'XLS' ? 'xlsx' : exportType.toLowerCase())
          );
          //this.spinner.spin(false);
        },
        error => {
          //this.spinner.spin(false);
        }
      );
  }

  trackId(index: number, item: IRightalign) {
    return item.id;
  }

  registerChangeInRightaligns() {
    this.eventSubscriber = this.eventManager.subscribe('rightalignListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRightaligns(data: IRightalign[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.rightaligns = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
  onSearchSubmit(searchObject: object) {
    this.pageData.currentPage = 0;
    this.searchObject = this.getSearchObject();
    this.changeStateParams();
    this.loadAll();
  }
  public resetSearchFilter() {
    this.searchFilterForm.patchValue({
      id: '',
      sdf: ''
    });
    this.getSearchObject();
  }
  reLoad() {
    this.searchObject = this.getSearchObject();
  }
  public getSearchObject(): object {
    const searchObject: any = {};
    if (this.searchFilterForm.value['id']) {
      searchObject[`id.equals`] = this.searchFilterForm.value['id'];
    }
    if (this.searchFilterForm.value['sdf'].trim()) {
      searchObject[`sdf.contains`] = this.searchFilterForm.value['sdf'].trim();
    }
    return searchObject;
  }
}
