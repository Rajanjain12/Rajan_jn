import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-thankyou',
  templateUrl: './thankyou.component.html',
  styleUrls: ['./thankyou.component.scss']
})
export class ThankyouComponent implements OnInit {
  content: String;
  pageUrl: string;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation.extras.state.pageUrl === '/auth/reset-password') {
      this.content = 'Your password has been updated.';
    }
  }

  ngOnInit() {
    this.redirection(this.router);
  }
  redirection(router: Router) {
    let timeleft = 10;
    const downloadTimer = setInterval(() => {
      // document.getElementById('countdown').innerHTML = 'You will be redirected to login page in ' + timeleft + ' seconds';
      timeleft -= 1;
      if (timeleft <= 0) {
        router.navigate(['/auth/login']);
        clearInterval(downloadTimer);
      }
    }, 1000);
  }
}
