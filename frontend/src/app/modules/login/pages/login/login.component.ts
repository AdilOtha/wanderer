import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private route:ActivatedRoute, private authServie: AuthService, private router: Router) { }


  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if(token!=null) {
      this.authServie.addToken(token);
      this.router.navigate(['/']);
    }
  }

  onClick() {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  }

}
