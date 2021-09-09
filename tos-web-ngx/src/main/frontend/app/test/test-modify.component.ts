import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TestService } from '../service/test.service';

import { TestModel } from '../model/test.model';
@Component(
{
  templateUrl: './test-modify.component.htm'
})
export class TestModifyComponent implements OnInit 
{
  form =
  {
    data: 
    {
      hello: <string> null,
      world: <number> null,
    },
    options:
    {
      test: <TestModel> null
    }
  };
  
  constructor(private route: ActivatedRoute, private router: Router, private testService:TestService) 
  {
      
  }
  
  reset()
  {
    if(this.form.options.test!=null)
    {
      const t = this.form.options.test;
    
      this.form.data.hello = t.hello;
      this.form.data.world = t.world;
    }else
    {
      this.form.data.hello = null;
      this.form.data.world = null;
    }
  }
  
  onSubmit()
  {
    const owner = this;
    if(this.form.options.test!=null)
    {
      this.testService.updateTestByTestId(this.form.options.test.testId, this.form.data.hello, this.form.data.world).subscribe(res=>
      {
        this.router.navigate(['../../../complete', 'modify'], {relativeTo: this.route});
      });
    }else
    {
      this.testService.addTest(this.form.data.hello, this.form.data.world).subscribe(res=>
      {
        this.router.navigate(['../../complete', 'add'], {relativeTo: this.route});

      });
    }
  }
  
  ngOnInit()
  {
    this.form.options.test = this.route.snapshot.data['test'];
    this.reset();
  }
}
