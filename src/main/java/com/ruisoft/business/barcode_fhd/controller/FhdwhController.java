package com.ruisoft.business.barcode_fhd.controller;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruisoft.base.controller.BaseController;
import com.ruisoft.business.barcode_fhd.service.FhdwhService;
import com.ruisoft.common.CommonUtil;
import com.ruisoft.core.json.JSONMap;
import com.ruisoft.core.param.RequestUtil;

/**
 * 
 * @author WWL
 *
 */
@Controller
@RequestMapping("/fhdwh/")
public class FhdwhController extends BaseController{

	private static final Logger LOG = Logger.getLogger(FhdwhController.class);

	@Autowired
	private FhdwhService fhdwhService;
	
	@RequestMapping("query")
	public void query() {
		JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		if (queryData == null)
			queryData = getQueryData();
		JSONObject data = queryData.getParamsData();
		if(data.has("order_ckdh")){
			String ckdh = data.getString("order_ckdh");
			if(ckdh==null){
				ckdh="";
			}
			data.put("order_ckdh", ckdh);
			queryData.setParamsData(data);
		}
		try {
			List<JSONObject> result = fhdwhService.searchForPage(queryData);
			queryResponse(queryData, result);
		} catch (Exception e) {
			LOG.error("查询发货单数据错误！", e);
			json.put("status", "-2");
			json.put("msg", "执行查询发货单操作发生错误");
		} finally {
			try {
				response(json);
			} catch (IOException e) {
				LOG.error("查询返回信息时发生错误", e);
			}
			queryData = null;
		}
	}
	
	@RequestMapping("saveFhd")
	public void saveFhd() {
		JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
		try {
			JSONObject reqData = getReqData();
			JSONObject result = new JSONObject();
			result = fhdwhService.saveFhd(reqData);
			json.put("result", result);
			json.put("status", "1");
		} catch (Exception e) {
			LOG.error("保存发货单数据信息发生错误", e);
			json.put("status", "-2");
			json.put("msg", "保存发货单数据信息发生错误");
		} finally {
			try {
				response(json);
			} catch (IOException e) {
				LOG.error("返回信息时发生错误", e);
			}
		}
	}
	
	@RequestMapping("deleteFhd/{id}")
    public void deleteFhd(@PathVariable String id) throws Exception{
        int status = fhdwhService.deleteFhd(id);
        JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
        json.put("status", status);
        response(json);
    }
	
	 @RequestMapping("updateFhd")
	   public void updateFhd() {
	       JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
	       try {
	           JSONObject reqData = getReqData();
	           JSONObject result = new JSONObject();
	           result = fhdwhService.updateFhd(reqData);
	           json.put("data", result);
	           json.put("status", "1");
	       } catch (Exception e) {
	           LOG.error("更新发货单数据发生错误", e);
	           json.put("status", "-2");
	           json.put("msg", "更新发货单数据发生错误");
	       } finally {
	           try {
	               response(json);
	           } catch (IOException e) {
	               LOG.error("返回信息时发生错误", e);
	           }
	       }
	   }
	 /**
		 * 初始化扫码发货页面信息
		 */
		@RequestMapping("queryFhdInfoByXml")
		public void queryFhdInfoByXml() {
			List<JSONObject> fhdlist=null;
			JSONObject jsn = new JSONObject();
			String currentPagestr = request.getParameter("currentPage")==null?"1":request.getParameter("currentPage");
		 	String onePageJls = request.getParameter("onePageJls")==null?"1":request.getParameter("onePageJls");
		 	int currentPage=Integer.parseInt(currentPagestr);
		 	int pageCount=Integer.parseInt(onePageJls);//一页要展示记录数
		 	int rowend=currentPage*pageCount;
		 	int rowstart=pageCount*(currentPage-1)+1;
		 	jsn.put("rowend", rowend);
			jsn.put("rowstart", rowstart);
			try {
				fhdlist = fhdwhService.queryFhdInfoByXml(jsn);
			} catch (Exception e) {
				LOG.error("查询发货单信息发生错误", e);
			} 
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			for(int i=0;i<fhdlist.size();i++){
				JSONObject json = (JSONObject) fhdlist.get(i);
				System.out.println("json= "+json);
				String order_ckdh = json.get("order_ckdh").toString();
				String order_fhrq = json.get("order_fhrq").toString();
				String order_khmc = json.get("order_khmc").toString();
				String order_mdh = json.get("order_mdh").toString();
				String order_gh = json.get("order_gh").toString();
				String order_fh = json.get("order_fh").toString();
				//根据出库单号查询明细表crm_order_detail，获得已扫描数量
				String sql="select * from crm_order_detail where orderDetail_ckdh='"+order_ckdh+"'";
				int ysmsl=0;
				try {
					ysmsl=fhdwhService.queryFhdFenyeInfoByXml(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				xmlstr.append("<item order_ckdh='"+order_ckdh
						+"' order_fhrq='"+order_fhrq
						+"' order_khmc='"+order_khmc
						+"' order_mdh='"+order_mdh
						+"' order_gh='"+order_gh
						+"' order_fh='"+order_fh
						+"' order_ysmsl='"+ysmsl
						+"'></item>");
			}
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 /**
		 * 初始化扫码发货分页信息
		 */
		@RequestMapping("queryFhdFenyeInfoByXml")
		public void queryFhdFenyeInfoByXml() {
			String sql="SELECT * FROM CRM_ORDER where order_fhzt='1'";
			int zjls =0;
			try {
				zjls=fhdwhService.queryFhdFenyeInfoByXml(sql);
			} catch (Exception e) {
				LOG.error("查询发货单信息发生错误", e);
			} 
			String onePageJlsstr=request.getParameter("onePageJls")==null?"1":request.getParameter("onePageJls");
			//一页记录数
			int onePageJls=Integer.parseInt(onePageJlsstr);
			//总页数
			int totalPagenum = zjls%onePageJls==0?zjls/onePageJls:zjls/onePageJls + 1 ;
			
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			xmlstr.append("<fenyeInfo zjls='"+zjls
					+"' onePageJls='"+onePageJls
					+"' totalPagenum='"+totalPagenum
					+"'></fenyeInfo>");
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * 通过客户编号查询条码信息
		 */
		@RequestMapping("queryBarcodeInfoByXml")
		public void queryBarcodeInfoByXml() {
			List<JSONObject> barcodelist=null;
			JSONObject jsn = new JSONObject();
			String khbh = request.getParameter("khbh")==null?"":request.getParameter("khbh");
			jsn.put("khbh", khbh);
			try {
				barcodelist = fhdwhService.queryBarcodeInfoByXml(jsn);
			} catch (Exception e) {
				LOG.error("查询条码信息发生错误", e);
			} 
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			for(int i=0;i<barcodelist.size();i++){
				JSONObject json = (JSONObject) barcodelist.get(i);
				System.out.println("json= "+json);
				String barcode_khmc = json.get("barcode_khmc").toString();
				String barcode_sc = json.get("barcode_sc").toString();
				String barcode_khbh = json.get("barcode_khbh").toString();
				String barcode_upc = json.get("barcode_upc").toString();
				String barcode_ks = json.get("barcode_ks").toString();
				String barcode_gg = json.get("barcode_gg").toString();
				
				String barcode_pcd = json.get("barcode_pcd").toString();
				String barcode_zxk = json.get("barcode_zxk").toString();
				String barcode_pj = json.get("barcode_pj").toString();
				String barcode_djz = json.get("barcode_djz").toString();
				String barcode_mjz = json.get("barcode_mjz").toString();
				String barcode_djt = json.get("barcode_djt").toString();
				String barcode_bmzt = json.get("barcode_bmzt").toString();
				String barcode_bmztyw = json.get("barcode_bmztyw").toString();
				String barcode_bzfs = json.get("barcode_bzfs").toString();
				xmlstr.append("<item barcode_khmc='"+barcode_khmc
						+"' barcode_sc='"+barcode_sc
						+"' barcode_khbh='"+barcode_khbh
						+"' barcode_upc='"+barcode_upc
						+"' barcode_ks='"+barcode_ks
						+"' barcode_gg='"+barcode_gg
						+"' barcode_pcd='"+barcode_pcd
						+"' barcode_zxk='"+barcode_zxk
						+"' barcode_pj='"+barcode_pj
						+"' barcode_djz='"+barcode_djz
						+"' barcode_mjz='"+barcode_mjz
						+"' barcode_djt='"+barcode_djt
						+"' barcode_bmzt='"+barcode_bmzt
						+"' barcode_bmztyw='"+barcode_bmztyw
						+"' barcode_bzfs='"+barcode_bzfs
						+"'></item>");
			}
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@RequestMapping("saveSmxx")
		public void saveSmxx() {
			JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
			try {
				
				JSONObject reqData = getReqData();
				JSONObject result = new JSONObject();
				String orderckdh=reqData.get("orderckdh")+"";
				String orderkhbhs=reqData.get("orderkhbhs")+"";
				String ordermtgs=reqData.get("ordermtgs")+"";
				result=fhdwhService.saveSmxx(orderckdh,orderkhbhs,ordermtgs);
				json.put("result", result);
				json.put("status", "1");
			} catch (Exception e) {
				LOG.error("保存发货单数据信息发生错误", e);
				json.put("status", "-2");
				json.put("msg", "保存发货单数据信息发生错误");
			} finally {
				try {
					response(json);
				} catch (IOException e) {
					LOG.error("返回信息时发生错误", e);
				}
			}
		}
		
		 /**
		 * 通过ckdh查询马托个数
		 */
		@RequestMapping("queryMtgsByCkdh")
		public void queryMtgsByCkdh() {
			String ckdh=request.getParameter("ckdh")==null?"":request.getParameter("ckdh");
			String sql="select ifnull(max(orderDetail_mtgs),0) as mtgs from crm_order_detail where orderDetail_ckdh='"+ckdh+"'";
			String mtgs ="0";
			try {
				Map<String, Object> map=fhdwhService.queryMtgsByCkdh(sql);
				mtgs=map.get("mtgs")+"";
			} catch (Exception e) {
				LOG.error("查询发货单信息发生错误", e);
			} 
			
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			xmlstr.append("<item mtgs='"+mtgs
					+"'></item>");
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 通过出库单号查询明细汇总相关信息
		 */
		@RequestMapping("getDetailsum")
		public void getDetailsum() {
			List<JSONObject> detailsumlist=null;
			JSONObject jsn = new JSONObject();
			String order_ckdh = request.getParameter("order_ckdh")==null?"":request.getParameter("order_ckdh");
			jsn.put("order_ckdh", order_ckdh);
			try {
				detailsumlist = fhdwhService.getDetailsum(jsn);
			} catch (Exception e) {
				LOG.error("查询扫码明细信息发生错误", e);
			} 
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			for(int i=0;i<detailsumlist.size();i++){
				JSONObject json = (JSONObject) detailsumlist.get(i);
				System.out.println("json= "+json);
				String detailsum_khbh = json.get("detailsum_khbh").toString();
				String barcode_sc = json.get("barcode_sc").toString();
				String barcode_upc = json.get("barcode_upc").toString();
				String barcode_gg = json.get("barcode_gg").toString();
				String detailsum_sl = json.get("detailsum_sl").toString();
				String barcode_bmzt = json.get("barcode_bmzt").toString();
				String barcode_djz = json.get("barcode_djz").toString();
				String barcode_mjz = json.get("barcode_mjz").toString();
				String detailsum_zjz = json.get("detailsum_zjz").toString();
				String detailsum_zmz = json.get("detailsum_zmz").toString();
				String barcode_djt = json.get("barcode_djt").toString();
				String detailsum_ztj = json.get("detailsum_ztj").toString();
				String barcode_bzfs = json.get("barcode_bzfs").toString();
				xmlstr.append("<item detailsum_khbh='"+detailsum_khbh
						+"' barcode_sc='"+barcode_sc
						+"' barcode_upc='"+barcode_upc
						+"' barcode_gg='"+barcode_gg
						+"' detailsum_sl='"+detailsum_sl
						+"' barcode_bmzt='"+barcode_bmzt
						+"' barcode_djz='"+barcode_djz
						+"' barcode_mjz='"+barcode_mjz
						+"' detailsum_zjz='"+detailsum_zjz
						+"' detailsum_zmz='"+detailsum_zmz
						+"' barcode_djt='"+barcode_djt
						+"' detailsum_ztj='"+detailsum_ztj
						+"' barcode_bzfs='"+barcode_bzfs
						+"'></item>");
			}
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@RequestMapping("qrfhd")
		public void qrfhd() {
			JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
			try {
				
				JSONObject reqData = getReqData();
				String orderckdh=reqData.get("orderckdh")+"";
				String sql="update crm_order set order_fhzt='2' where order_ckdh='"+orderckdh+"'";
				int result=fhdwhService.qrfhd(sql);
				json.put("result", result);
				json.put("status", "1");
			} catch (Exception e) {
				LOG.error("确认发货单信息发生错误", e);
				json.put("status", "-2");
				json.put("msg", "确认发货单信息发生错误");
			} finally {
				try {
					response(json);
				} catch (IOException e) {
					LOG.error("返回信息时发生错误", e);
				}
			}
		}
		/**
		 * 通过出库单号查询明细
		 */
		@RequestMapping("queryDetailInfoByXml")
		public void queryDetailInfoByXml() {
			List<JSONObject> barcodelist=null;
			JSONObject jsn = new JSONObject();
			String ckdh = request.getParameter("ckdh")==null?"":request.getParameter("ckdh");
			jsn.put("ckdh", ckdh);
			try {
				barcodelist = fhdwhService.queryDetailInfoByXml(jsn);
			} catch (Exception e) {
				LOG.error("查询发货单明细发生错误", e);
			} 
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			for(int i=0;i<barcodelist.size();i++){
				JSONObject json = (JSONObject) barcodelist.get(i);
				System.out.println("json= "+json);
				String barcode_khmc = json.get("barcode_khmc").toString();
				String barcode_sc = json.get("barcode_sc").toString();
				String barcode_khbh = json.get("barcode_khbh").toString();
				String barcode_upc = json.get("barcode_upc").toString();
				String barcode_ks = json.get("barcode_ks").toString();
				String barcode_gg = json.get("barcode_gg").toString();
				
				String barcode_pcd = json.get("barcode_pcd").toString();
				String barcode_zxk = json.get("barcode_zxk").toString();
				String barcode_pj = json.get("barcode_pj").toString();
				String barcode_djz = json.get("barcode_djz").toString();
				String barcode_mjz = json.get("barcode_mjz").toString();
				String barcode_djt = json.get("barcode_djt").toString();
				String barcode_bmzt = json.get("barcode_bmzt").toString();
				String barcode_bmztyw = json.get("barcode_bmztyw").toString();
				String barcode_bzfs = json.get("barcode_bzfs").toString();
				String orderdetail_id = json.get("orderdetail_id").toString();
				xmlstr.append("<item barcode_khmc='"+barcode_khmc
						+"' barcode_sc='"+barcode_sc
						+"' barcode_khbh='"+barcode_khbh
						+"' barcode_upc='"+barcode_upc
						+"' barcode_ks='"+barcode_ks
						+"' barcode_gg='"+barcode_gg
						+"' barcode_pcd='"+barcode_pcd
						+"' barcode_zxk='"+barcode_zxk
						+"' barcode_pj='"+barcode_pj
						+"' barcode_djz='"+barcode_djz
						+"' barcode_mjz='"+barcode_mjz
						+"' barcode_djt='"+barcode_djt
						+"' barcode_bmzt='"+barcode_bmzt
						+"' barcode_bmztyw='"+barcode_bmztyw
						+"' barcode_bzfs='"+barcode_bzfs
						+"' orderdetail_id='"+orderdetail_id
						+"'></item>");
			}
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@RequestMapping("deleteDetailByid")
		public void deleteDetailByid() {
			JSONMap<String, Object> json = new JSONMap<String, Object>(JSONMap.TYPE.OBJECT);
			try {
				
				JSONObject reqData = getReqData();
				String orderDetail_id=reqData.get("orderDetail_id")+"";
				String orderDetail_ckdh=reqData.get("orderDetail_ckdh")+"";
				String sql="delete from crm_order_detail where orderDetail_id='"+orderDetail_id+"'";
				fhdwhService.deleteDetailByid(sql,orderDetail_ckdh);
				json.put("status", "1");
			} catch (Exception e) {
				LOG.error("删除扫码明细信息发生错误", e);
				json.put("status", "-2");
				json.put("msg", "删除扫码明细信息发生错误");
			} finally {
				try {
					response(json);
				} catch (IOException e) {
					LOG.error("返回信息时发生错误", e);
				}
			}
		}
		 /**
		 * 判断出库单号是否重复
		 */
		@RequestMapping("judgeCkdhRepeat")
		public void judgeCkdhRepeat() {
			String ckdhh=request.getParameter("ckdhh")==null?"1":request.getParameter("ckdhh");
			String sql="select * from crm_order where order_ckdh='"+ckdhh+"'";
			int jls =0;
			try {
				jls=fhdwhService.judgeCkdhRepeat(sql);
			} catch (Exception e) {
				LOG.error("查询发货单信息发生错误", e);
			} 
			
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			xmlstr.append("<item jls='"+jls
					+"'></item>");
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		@RequestMapping("queryBarcodeInfoByXmlnew")
		public void queryBarcodeInfoByXmlnew() {
			String khbh = request.getParameter("khbh")==null?"":request.getParameter("khbh");
			String[] str=khbh.split(",");
			StringBuffer xmlstr = new StringBuffer();
			xmlstr.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			xmlstr.append("<root>");
			for(int m=0;m<str.length;m++){
				JSONObject jsn = new JSONObject();
				jsn.put("khbh", str[m]);
				List<JSONObject> barcodelist=null;
				try {
					barcodelist = fhdwhService.queryBarcodeInfoByXml(jsn);
				} catch (Exception e) {
					LOG.error("查询条码信息发生错误", e);
				} 
				if (barcodelist.size()==0) {
					xmlstr.append("<item barcode_khmc=''"+
							" barcode_sc=''"+
							" barcode_khbh='"+str[m]+"'"+
							" barcode_upc=''"+
							" barcode_ks=''"+
							" barcode_gg=''"+
							" barcode_pcd=''"+
							" barcode_zxk=''"+
							" barcode_pj=''"+
							" barcode_djz=''"+
							" barcode_mjz=''"+
							" barcode_djt=''"+
							" barcode_bmzt=''"+
							" barcode_bmztyw=''"+
							" barcode_bzfs=''"+
							"></item>");
				}else{
					JSONObject json = (JSONObject) barcodelist.get(0);
					System.out.println("json= "+json);
					String barcode_khmc = json.get("barcode_khmc").toString();
					String barcode_sc = json.get("barcode_sc").toString();
					String barcode_khbh = json.get("barcode_khbh").toString();
					String barcode_upc = json.get("barcode_upc").toString();
					String barcode_ks = json.get("barcode_ks").toString();
					String barcode_gg = json.get("barcode_gg").toString();
					
					String barcode_pcd = json.get("barcode_pcd").toString();
					String barcode_zxk = json.get("barcode_zxk").toString();
					String barcode_pj = json.get("barcode_pj").toString();
					String barcode_djz = json.get("barcode_djz").toString();
					String barcode_mjz = json.get("barcode_mjz").toString();
					String barcode_djt = json.get("barcode_djt").toString();
					String barcode_bmzt = json.get("barcode_bmzt").toString();
					String barcode_bmztyw = json.get("barcode_bmztyw").toString();
					String barcode_bzfs = json.get("barcode_bzfs").toString();
					xmlstr.append("<item barcode_khmc='"+barcode_khmc
							+"' barcode_sc='"+barcode_sc
							+"' barcode_khbh='"+barcode_khbh
							+"' barcode_upc='"+barcode_upc
							+"' barcode_ks='"+barcode_ks
							+"' barcode_gg='"+barcode_gg
							+"' barcode_pcd='"+barcode_pcd
							+"' barcode_zxk='"+barcode_zxk
							+"' barcode_pj='"+barcode_pj
							+"' barcode_djz='"+barcode_djz
							+"' barcode_mjz='"+barcode_mjz
							+"' barcode_djt='"+barcode_djt
							+"' barcode_bmzt='"+barcode_bmzt
							+"' barcode_bmztyw='"+barcode_bmztyw
							+"' barcode_bzfs='"+barcode_bzfs
							+"'></item>");
				}
			}
			xmlstr.append("</root>");
			try {
				response.setContentType("text/xml;charset=utf-8");
				response.getWriter().write(xmlstr.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
