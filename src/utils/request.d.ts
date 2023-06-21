export interface Http{
  get<T>(url:string,params?:object):Promise<T>
  post<T>(url:string,params?:object):Promise<T>
}