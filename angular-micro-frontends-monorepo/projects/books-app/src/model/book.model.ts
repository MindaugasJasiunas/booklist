export class Book{
  constructor(private id: number, private _author: string, private _title: string, private _ISBN: string, private _pages: number, private _hardtop: boolean, private _ebook: boolean, private _image?: string){}

  get author(){
    return this._author;
  }

  get title(){
    return this._title;
  }

  get pages(){
    return this._pages;
  }

  get isHardtop(){
    return this._hardtop;
  }

  get isEbook(){
    return this._ebook;
  }

  get ISBN(){
    return this._ISBN;
  }

  get image(): string | undefined{
    return this._image;
  }
}
