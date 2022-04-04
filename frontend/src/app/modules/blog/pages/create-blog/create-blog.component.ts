import { Component, OnInit } from '@angular/core';
import { BlogService } from 'src/app/data/service/blog-service/blog.service';

@Component({
  selector: 'app-create-blog',
  templateUrl: './create-blog.component.html',
  styleUrls: ['./create-blog.component.scss']
})
export class CreateBlogComponent implements OnInit {

  constructor(private blogservice:BlogService) { }

  ngOnInit(): void {
    this.blogservice.getBlogs().subscribe({
      next:(data:any) => {
        console.log(data);
      } 
    })
  }

}
