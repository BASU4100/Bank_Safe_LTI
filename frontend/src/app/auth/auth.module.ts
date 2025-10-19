import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AuthRoutingModule } from "./auth-routing.module";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { AuthComponent } from "./auth.component";
import { LoginComponent } from "./components/login/login.component";
import { LogoutComponent } from "./components/logout/logout.component";
import { UserComponent } from "./components/user/user.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [
    AuthComponent,
    LoginComponent,
    LogoutComponent,
    UserComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule
  ],
  exports: [],
  bootstrap: [AuthComponent]
})
export class AuthModule {}
