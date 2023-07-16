import React from "react";

class Shop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            // id: 0,
            // name: "",
            // desc: "",
            views: 0
        }
        this.updateView = this.updateView.bind(this)
    }

    updateView() {
        this.setState({views : this.state.views +1})
    }

    render() {
        return (
            <shop>
                <img/>
                <div className={"shopName"}></div>
                <div>shop Description</div>
                <div className={"productsList"}></div>
                <div>views {this.state.views}</div>
                <button onClick={this.updateView}></button>
            </shop>
        )
    }
}

export default Shop