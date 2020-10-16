import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SimpleInventorySharedModule } from 'app/shared/shared.module';
import { SimpleInventoryCoreModule } from 'app/core/core.module';
import { SimpleInventoryAppRoutingModule } from './app-routing.module';
import { SimpleInventoryHomeModule } from './home/home.module';
import { SimpleInventoryEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    SimpleInventorySharedModule,
    SimpleInventoryCoreModule,
    SimpleInventoryHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SimpleInventoryEntityModule,
    SimpleInventoryAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class SimpleInventoryAppModule {}
