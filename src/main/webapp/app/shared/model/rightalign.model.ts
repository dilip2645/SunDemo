export interface IRightalign {
  id?: number;
  sdf?: string;
}

export class Rightalign implements IRightalign {
  constructor(public id?: number, public sdf?: string) {}
}
