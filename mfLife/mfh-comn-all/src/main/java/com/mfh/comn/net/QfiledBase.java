package com.mfh.comn.net;
import org.apache.commons.lang3.StringUtils;

/**
 * 2013-06-15 移入公共类库，去掉了dom4j之类的导出
 * 功能：电子表单公共属性累
 * @author administrator
 * 重大修改说明：扩展属性 （修改处均注明作者以及时间）
 */
public class QfiledBase{
    protected String name;//属性名称
    protected String type;//属性类型,对应jdbc类型的字符串定义,参见TypeAdapter中的Map2SqlTypes的key值。
    private String colspan = "1"; // 一个td的横跨度,缺省为1
    private String rowspan = "1"; // 一个td的列跨度,缺省为1
    private String valign = "top";// top、bottom、middle,缺省为left
    private String align = "center";// left、center、right，,缺省为center?
    private String caption;//名称，可覆盖模型文件中已经有的    
    public static int DEFAULT_SIZE = 20;//注意要同下面的size
    protected String size = "20";//注意同上面的size,用于指定显示的宽度和高度，宽度单位为字元数或百分比,高度单元为px(iframe)或行数(textArea)，缺省为20. add by zyz.
	protected String hidden = "false";//缺省为null，代表false。
	protected short order = -1;//用于排序。add by zyz
	private int width = DEFAULT_SIZE;//改值是从size中临时解析来的，实际并未存储。宽度，是size的一部分,size还可能包括高度.
	protected boolean checked=true;//用于界面上的选中操作，add by feil
	protected boolean groupchecked=false;//用于分组界面上的是否选中操作，add by gaob 09.03.24
	//add by gaob 09.06.25
	protected String group;// 组编号唯一标示 09.06.25
	protected String groupName;//组名 
	protected String maxChar = "0" ; //字段显示字符数 09.08.26
	protected String styleClass;
	
	public QfiledBase(){
	    super();
	}	
	
	/**
	 * 获取多值录入分割符
	 * @return
	 * @author zhangyz created on 2012-5-19
	 */
    public String getMutiSeparator(){
        throw new RuntimeException("不支持的属性");
    }
    
    /**
     * 设置多值录入分隔符
     * @param mutiSeparator
     * @author zhangyz created on 2012-5-19
     */
    public void setMutiSeparator(String mutiSeparator){
        throw new RuntimeException("不支持的属性");
    }
    
    public String getFormat(){
        throw new RuntimeException("不支持的属性");
    }
    
    public void setFormat(String format){
        throw new RuntimeException("不支持的属性");
    }

    /**
	 * @param name 名称
	 * @param caption 描述
	 * @param type 类型
	 */
	public QfiledBase(String name, String caption, String type) {
        super();
        this.name = name;
        this.caption = caption;
        this.type = type;
    }

    //add end 
    public void copy(QfiledBase source){
    	this.align = source.getAlign();
    	this.caption = source.getCaption();
    	this.colspan = source.getColspan();
    	this.rowspan = source.getRowspan();
    	this.hidden = source.getHidden();
    	this.size = source.getSize();
    	this.order = source.getOrder();
    	this.name = source.getName();
    	this.valign = source.getValign();
    	this.width = source.getWidthInt();
    	this.type = source.getType();
    	this.setMutiSeparator(source.getMutiSeparator());
    	this.maxChar = source.getMaxChar();
        setStyleClass(source.getStyleClass());
    }

    @Override
    public String toString(){
    	StringBuffer rv = new StringBuffer();
        rv.append("[align=" + this.getAlign());
        rv.append(",caption=" + this.getCaption());
        rv.append(",colspan=" + this.getColspan());
        rv.append(",rowspan=" + this.getRowspan());
        rv.append(",hidden=" + this.getHidden());
        rv.append(",size=" + this.getSize());
        rv.append(",order=" + this.getOrder());
        rv.append(",name=" + this.getName());
        rv.append(",valign=" + this.getValign());
        rv.append(",width=" + this.getWidthInt());
        rv.append(",checked=" + this.getChecked());
    	rv.append("]");
    	return rv.toString();
    }
    
    /**
     * 导出成json格式
     * @return
     * @author zhangyz created on 2012-4-5
     */
    /*public JSONObject exportToJson(){
        JSONObject jsonProp = new JSONObject();
        jsonProp.put("name", getName());       
        jsonProp.put("caption", getCaption());
        jsonProp.put("type", getType());
        if (StringUtils.isNotBlank(getGroup())) {
            jsonProp.put("groupName", getGroupName());
            jsonProp.put("group", getGroup());
        } 
        jsonProp.put("align", this.getAlign());
        if (StringUtils.isNotBlank(getColspan()))      
            jsonProp.put("colspan", getColspan());
        if (StringUtils.isNotBlank(getRowspan()))        
            jsonProp.put("rowspan", getRowspan());
        if (StringUtils.isNotBlank(getStyleClass()))
            jsonProp.put("styleClass", getStyleClass());
        return jsonProp;
    }*/
    
    /**
     * 导出成xml格式
     * @param eleOut
     * @author zhangyz created on 2012-4-5
     */
    /*public void exportToXml(Element eleOut){
        eleOut.addAttribute("name", getName());       
        eleOut.addAttribute("caption", getCaption());
        eleOut.addAttribute("type", getType());
        if (StringUtils.isNotBlank(getGroup())) {
            eleOut.addAttribute("groupName", getGroupName());
            eleOut.addAttribute("group", getGroup());
        }
        eleOut.addAttribute("align", this.getAlign());
        if (StringUtils.isNotBlank(getColspan()))      
            eleOut.addAttribute("colspan", getColspan());
        if (StringUtils.isNotBlank(getRowspan()))        
            eleOut.addAttribute("rowspan", getRowspan());
        if (StringUtils.isNotBlank(getStyleClass()))
            eleOut.addAttribute("styleClass", getStyleClass());
    }*/

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
    
    public String getSize() {
		return size;
	}
    
    public String getWidth(){
    	//String[] rets = StringUtils.splitByWholeSeparator(size, ",");
    	//return rets[0];
    	return Integer.toString(width);
    }
    
    public int getWidthInt(){
    	return width;
    }
    
    private static String RADIO_SEG = "%";//原来想通过百分比固定列宽度，但由于总体宽度没限定，效果还是不好。update by zyz 2008-09-24
	private double multi = 7.4;//原来表格列宽度为size（字符数）,换算成像素点数。最终表格列表宽度写死.update by zyz 2008-09-26
	/**
	 * @deprecated
	 * @param total
	 * @return
	 */
    @Deprecated
    public String getWidthRadio(int total){
    	float radio = ((float)width / total) * 100;
    	return radio + RADIO_SEG;   
    }
    /**
     * @deprecated
     * @param width
     * @param total
     * @return
     */
    @Deprecated
    public static String getWidthRadio(int width,int total){
    	float radio = ((float)width / total) * 100;
    	return radio + RADIO_SEG;   
    }
    
    /**
     * 把input的size宽度换算成td的像素点宽度。
     * @return
     */
    public String getWidthPixesStr(){
    	return Double.toString(width * multi);
    }
    public double getWidthPixesInt(){
    	return width * multi;
    }
    
    /**
     * 获取编辑框的高度。
     * @return
     */    
    public String getHeight(){
    	String[] rets = StringUtils.splitByWholeSeparator(size, ",");
    	if (rets.length == 2)
    		return rets[1];
        return "300";
    }
    
	public void setSize(String size) {
		this.size = size;
		if ((size == null) || (size.length() == 0))
			width = DEFAULT_SIZE;
		else{
	    	String[] rets = StringUtils.splitByWholeSeparator(size, ",");
	    	try{
	    	    width = Integer.parseInt(rets[0]);
	    	}
	    	catch(Exception ex){
	    	    ;
	    	}
		}
	}
	
    public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public short getOrder() {
		return order;
	}
    
	public void setOrder(short order) {
		this.order = order;
	}
	
	public boolean getChecked(){
		return this.checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
    /**
     * @return Returns the align.
     */
    public String getAlign() {
        return align;
    }

    /**
     * @param align
     *            The align to set.
     */
    public void setAlign(String align) {
        this.align = align;
    }

    /**
     * @return Returns the caption.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption
     *            The caption to set.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return Returns the colspan.
     */
    public String getColspan() {
        return colspan;
    }

    /**
     * @param colspan
     *            The colspan to set.
     */
    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the rowspan.
     */
    public String getRowspan() {
        return rowspan;
    }

    /**
     * @param rowspan
     *            The rowspan to set.
     */
    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    /**
     * @return Returns the valign.
     */
    public String getValign() {
        return valign;
    }

    /**
     * @param valign
     *            The valign to set.
     */
    public void setValign(String valign) {
        this.valign = valign;
    }

	public boolean getGroupchecked() {
		return groupchecked;
	}

	public void setGroupchecked(boolean groupchecked) {
		this.groupchecked = groupchecked;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMaxChar() {
		return maxChar;
	}

	public void setMaxChar(String maxChar) {
		this.maxChar = maxChar;
	}


}

